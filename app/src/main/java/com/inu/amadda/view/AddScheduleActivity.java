package com.inu.amadda.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.inu.amadda.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class AddScheduleActivity extends AppCompatActivity {

    private boolean isExpanded = false, isAlarmClicked;

    private ExpandableLayout expandable_alarm, expandable_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initialize();

        TextView textView = findViewById(R.id.tv_share);
        textView.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.alarm), null, null, null);

    }

    private void initialize() {
        RelativeLayout rl_alarm = findViewById(R.id.rl_alarm);
        RelativeLayout rl_share = findViewById(R.id.rl_share);
        expandable_alarm = findViewById(R.id.expandable_alarm);
        expandable_share = findViewById(R.id.expandable_share);

        rl_alarm.setOnClickListener(onClickListener);
        rl_share.setOnClickListener(onClickListener);
    }

    private void setExpandable(boolean clickAlarm) {
        if (isExpanded) {
            if (isAlarmClicked){
                if (clickAlarm) {
                    expandable_alarm.collapse();
                    isExpanded = false;
                }
                else {
                    expandable_alarm.collapse();
                    expandable_share.expand();
                    isExpanded = true;
                    isAlarmClicked = false;
                }
            }
            else {
                if (clickAlarm) {
                    expandable_alarm.expand();
                    expandable_share.collapse();
                    isExpanded = true;
                    isAlarmClicked = true;
                }
                else {
                    expandable_share.collapse();
                    isExpanded = false;
                }
            }
        }
        else {
            if (clickAlarm) {
                expandable_alarm.expand();
                isExpanded = true;
                isAlarmClicked = true;
            }
            else {
                expandable_share.expand();
                isExpanded = true;
                isAlarmClicked = false;
            }
        }
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.rl_alarm: {
                setExpandable(true);
                break;
            }
            case R.id.rl_share: {
                setExpandable(false);
                break;
            }
        }
    };

}

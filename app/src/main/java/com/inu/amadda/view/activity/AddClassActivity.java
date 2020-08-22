package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.contrarywind.view.WheelView;
import com.inu.amadda.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class AddClassActivity extends AppCompatActivity {

    private boolean isExpanded = false, isStartClicked;

    private EditText et_class_name, et_place;
    private TextView tv_start_day, tv_start_ampm, tv_start_time, tv_end_day, tv_end_ampm, tv_end_time;
    private ExpandableLayout expandable_start, expandable_end;
    private WheelView wheel_start, wheel_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        initialize();
        setToolbar();

    }

    private void initialize() {
        et_class_name = findViewById(R.id.et_class_name);

        RelativeLayout rl_start = findViewById(R.id.rl_start);
        rl_start.setOnClickListener(onClickListener);
        tv_start_day = findViewById(R.id.tv_start_day);
        tv_start_ampm = findViewById(R.id.tv_start_ampm);
        tv_start_time = findViewById(R.id.tv_start_time);

        expandable_start = findViewById(R.id.expandable_start);
        wheel_start = findViewById(R.id.wheel_start);

        RelativeLayout rl_end = findViewById(R.id.rl_end);
        rl_end.setOnClickListener(onClickListener);
        tv_end_day = findViewById(R.id.tv_end_day);
        tv_end_ampm = findViewById(R.id.tv_end_ampm);
        tv_end_time = findViewById(R.id.tv_end_time);

        expandable_end = findViewById(R.id.expandable_end);
        wheel_end = findViewById(R.id.wheel_end);

        et_place = findViewById(R.id.et_place);

        TextView btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(onClickListener);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.cancel);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn_image.setVisibility(View.GONE);

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("수업 추가");
    }

    private void setExpandable(boolean clickStart) {
        if (isExpanded) {
            if (isStartClicked){
                if (clickStart) {
                    expandable_start.collapse();
                    isExpanded = false;
                }
                else {
                    expandable_start.collapse();
                    expandable_end.expand();
                    isExpanded = true;
                    isStartClicked = false;
                }
            }
            else {
                if (clickStart) {
                    expandable_start.expand();
                    expandable_end.collapse();
                    isExpanded = true;
                    isStartClicked = true;
                }
                else {
                    expandable_end.collapse();
                    isExpanded = false;
                }
            }
        }
        else {
            if (clickStart) {
                expandable_start.expand();
                isExpanded = true;
                isStartClicked = true;
            }
            else {
                expandable_end.expand();
                isExpanded = true;
                isStartClicked = false;
            }
        }
    }

    private void sendClassInfo() {
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.rl_start: {
                setExpandable(true);
                break;
            }
            case R.id.rl_end: {
                setExpandable(false);
                break;
            }
            case R.id.btn_add: {
                sendClassInfo();
                break;
            }
        }
    };

}
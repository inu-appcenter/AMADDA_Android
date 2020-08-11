package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.inu.amadda.R;
import com.inu.amadda.util.DateUtils;

public class ScheduleListActivity extends AppCompatActivity {

    private static int PERSONAL = -1;
    private static int DAY = -2;

    private int share;
    private String group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        initialize();
        checkType();
        setToolbar();

    }

    private void initialize() {
        CardView cv_add_schedule = findViewById(R.id.cv_add_schedule);
        cv_add_schedule.setOnClickListener(onClickListener);
    }

    private void checkType() {
        Intent intent = getIntent();
        share = intent.getIntExtra("share", PERSONAL);
        group_name = intent.getStringExtra("group_name");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.back);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        if (share == PERSONAL || share == DAY){
            right_btn_image.setVisibility(View.GONE);
        }
        else {
            right_btn_image.setImageResource(R.drawable.setting_toolbar);
            right_btn_image.setOnClickListener(onClickListener);
        }

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        if (share == PERSONAL){
            title.setText("개인일정");
        }
        else if (share == DAY){
            title.setText(DateUtils.getToday());
        }
        else {
            title.setText(group_name);
        }
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.toolbar_right_image:{
                // 공유 그룹 수정으로
                break;
            }
            case R.id.cv_add_schedule: {
                Intent intent = new Intent(this, AddScheduleActivity.class);
                if (share == PERSONAL){
                    intent.putExtra("isPersonal", true);
                }
                else {
                    intent.putExtra("isPersonal", false);
                }
                startActivity(intent);
                break;
            }
        }
    };

}

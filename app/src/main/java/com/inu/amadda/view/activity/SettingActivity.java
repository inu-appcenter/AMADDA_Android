package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.util.PreferenceManager;

public class SettingActivity extends AppCompatActivity {

    private CheckBox cb_timetable, cb_calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar();
        initialize();
        getDefaultViewSetting();

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
        title.setText("설정");
    }

    private void initialize() {
        TextView tv_id = findViewById(R.id.tv_id);
        tv_id.setText(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null));

        RelativeLayout rl_profile = findViewById(R.id.rl_profile);
        rl_profile.setOnClickListener(onClickListener);

        cb_timetable = findViewById(R.id.cb_timetable);
        cb_timetable.setOnClickListener(onClickListener);

        cb_calendar = findViewById(R.id.cb_calendar);
        cb_calendar.setOnClickListener(onClickListener);

        RelativeLayout rl_push = findViewById(R.id.rl_push);
        rl_push.setOnClickListener(onClickListener);

        RelativeLayout rl_withdrawal = findViewById(R.id.rl_withdrawal);
        rl_withdrawal.setOnClickListener(onClickListener);
    }

    private void getDefaultViewSetting() {
        boolean checked = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.DEFAULT_VIEW,
                Constant.DefaultView.TIMETABLE);

        if (checked) {
            cb_timetable.setChecked(true);
            cb_calendar.setChecked(false);
        }
        else {
            cb_timetable.setChecked(false);
            cb_calendar.setChecked(true);
        }
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.rl_profile:{
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.rl_withdrawal:{

                break;
            }
            case R.id.rl_push:{
                Intent intent = new Intent(this, PushSettingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.cb_timetable: {
                if (cb_timetable.isChecked()) {
                    cb_calendar.setChecked(false);
                    PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.DEFAULT_VIEW,
                            Constant.DefaultView.TIMETABLE);
                }
                else {
                    cb_timetable.setChecked(true);
                }
                break;
            }
            case R.id.cb_calendar: {
                if (cb_calendar.isChecked()) {
                    cb_timetable.setChecked(false);
                    PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.DEFAULT_VIEW,
                            Constant.DefaultView.CALENDAR);
                }
                else {
                    cb_calendar.setChecked(true);
                }
                break;
            }
        }
    };

}

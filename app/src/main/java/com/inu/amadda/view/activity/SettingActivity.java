package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void withdrawal() {
        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        RetrofitInstance.getInstance().getService().Withdrawal(token).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            PreferenceManager.getInstance().resetSharedPreference(getApplicationContext());
                            // TODO Room 초기화
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("SettingActivity", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("SettingActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("SettingActivity", t.getMessage());
            }
        });
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
                withdrawal();
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

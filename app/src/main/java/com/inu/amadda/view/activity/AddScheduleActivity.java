package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity {

    private boolean isPersonal, isExpanded = false, isAlarmClicked;
    private String startDate = null, endDate = null;

    private ExpandableLayout expandable_alarm, expandable_share;
    private TextView tv_start_date, tv_start_ampm, tv_start_time, tv_end_date, tv_end_ampm, tv_end_time;
    private TimePickerView timePickerStart, timePickerEnd;
    private EditText et_name, et_place, et_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        checkType();
        setToolbar();
        initialize();
        setDefaultDateTime();
        setDateTimePicker();
        setAlarmWheelView();

    }

    private void checkType() {
        Intent intent = getIntent();
        isPersonal = intent.getBooleanExtra("isPersonal", true);

        LinearLayout layout = findViewById(R.id.ll_share);
        if (isPersonal){
            layout.setVisibility(View.GONE);
        }
        else {
            layout.setVisibility(View.VISIBLE);
        }
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
        if (isPersonal){
            title.setText("개인 일정 추가");
        }
        else {
            title.setText("공유 일정 추가");
        }
    }

    private void initialize() {
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_start_ampm = findViewById(R.id.tv_start_ampm);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_end_ampm = findViewById(R.id.tv_end_ampm);
        tv_end_time = findViewById(R.id.tv_end_time);

        RelativeLayout rl_start = findViewById(R.id.rl_start);
        RelativeLayout rl_end = findViewById(R.id.rl_end);
        RelativeLayout rl_alarm = findViewById(R.id.rl_alarm);
        RelativeLayout rl_share = findViewById(R.id.rl_share);
        expandable_alarm = findViewById(R.id.expandable_alarm);
        expandable_share = findViewById(R.id.expandable_share);

        rl_start.setOnClickListener(onClickListener);
        rl_end.setOnClickListener(onClickListener);
        rl_alarm.setOnClickListener(onClickListener);
        rl_share.setOnClickListener(onClickListener);

        et_name = findViewById(R.id.et_name);
        et_place = findViewById(R.id.et_place);
        et_memo = findViewById(R.id.et_memo);

        TextView btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(onClickListener);
    }

    private void setDefaultDateTime() {
        setDateTimeData(DateUtils.now, tv_start_date, tv_start_ampm, tv_start_time, true);
        setDateTimeData(DateUtils.now, tv_end_date, tv_end_ampm, tv_end_time, false);
    }

    private void setDateTimePicker() {
//        Calendar calendarRang = Calendar.getInstance();
//        calendarRang.set(2020, 0, 1);

        boolean[] type = new boolean[]{true, true, true, true, true, false};
        int color_ffffff = ContextCompat.getColor(this, R.color.color_ffffff);
        int color_1eb1fc = ContextCompat.getColor(this, R.color.color_1eb1fc);
        int color_cccccc = ContextCompat.getColor(this, R.color.color_cccccc);

        timePickerStart = new TimePickerBuilder(this, (date, v) -> {
            setDateTimeData(date, tv_start_date, tv_start_ampm, tv_start_time, true);
        })
                .setType(type)
                .setSubmitText("확인")
                .setCancelText("취소")
                .setTitleText("시작 날짜 설정")
                .setLabel("년","월","일","시","분", null)
                .setTitleBgColor(color_1eb1fc)
                .setTitleColor(color_ffffff)
                .setSubmitColor(color_ffffff)
                .setCancelColor(color_ffffff)
                .setTextColorCenter(color_1eb1fc)
                .setTextColorOut(color_cccccc)
                .setContentTextSize(20)
                .setLineSpacingMultiplier(4)
                .setDividerColor(color_1eb1fc)
//                .setRangDate(calendarRang, null)
                .setItemVisibleCount(3)
                .isCyclic(true)
                .isDialog(true)
//                .setOutSideCancelable(false)
                .build();

        timePickerEnd = new TimePickerBuilder(this, (date, v) -> {
            setDateTimeData(date, tv_end_date, tv_end_ampm, tv_end_time, false);
        })
                .setType(type)
                .setSubmitText("확인")
                .setCancelText("취소")
                .setTitleText("종료 날짜 설정")
                .setLabel("년","월","일","시","분", null)
                .setTitleBgColor(color_1eb1fc)
                .setTitleColor(color_ffffff)
                .setSubmitColor(color_ffffff)
                .setCancelColor(color_ffffff)
                .setTextColorCenter(color_1eb1fc)
                .setTextColorOut(color_cccccc)
                .setContentTextSize(20)
                .setLineSpacingMultiplier(4)
                .setDividerColor(color_1eb1fc)
//                .setRangDate(calendarRang, null)
                .setItemVisibleCount(3)
                .isCyclic(true)
                .isDialog(true)
//                .setOutSideCancelable(false)
                .build();
    }

    private void setDateTimeData(Date date, TextView tv_date, TextView tv_ampm, TextView tv_time, boolean isStart) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d", Locale.KOREAN);
        SimpleDateFormat ampmFormat = new SimpleDateFormat("a", Locale.KOREAN);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.KOREAN);

        tv_date.setText(dateFormat.format(date));
        tv_ampm.setText(ampmFormat.format(date));
        tv_time.setText(timeFormat.format(date));

        if(isStart) {
            startDate = DateUtils.dateFormat.format(date);
        }
        else {
            endDate = DateUtils.dateFormat.format(date);
        }
    }

    private void setAlarmWheelView() {
        int color_1eb1fc = ContextCompat.getColor(this, R.color.color_1eb1fc);
        int color_cccccc = ContextCompat.getColor(this, R.color.color_cccccc);

        WheelView wheel_alarm = findViewById(R.id.wheel_alarm);
        wheel_alarm.setItemsVisibleCount(3);
        wheel_alarm.setTextColorCenter(color_1eb1fc);
        wheel_alarm.setTextColorOut(color_cccccc);
        wheel_alarm.setTextSize(20);
        wheel_alarm.setLineSpacingMultiplier(3);
        wheel_alarm.setDividerColor(color_1eb1fc);
        wheel_alarm.setCyclic(false);

        List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("1시간 전");
        mOptionsItems.add("2시간 전");
        mOptionsItems.add("12시간 전");
        mOptionsItems.add("하루 전");

        wheel_alarm.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheel_alarm.setOnItemSelectedListener(index -> {
            //알람 보류
        });
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

    private void sendScheduleInfo() {
        AddScheduleModel addScheduleModel = new AddScheduleModel();
        addScheduleModel.setToken(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null));
        addScheduleModel.setSchedule_name(et_name.getText().toString());
        addScheduleModel.setStart(startDate);
        addScheduleModel.setEnd(endDate);
        addScheduleModel.setLocation(et_place.getText().toString());
//        addScheduleModel.setAlarm(null);
//        addScheduleModel.setShare(null);
        addScheduleModel.setMemo(et_memo.getText().toString());

        RetrofitInstance.getInstance().getService().AddSchedule(addScheduleModel).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success.equals("true")) {
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("AddScheduleActivity", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
//                else if (status == 400){
//                    Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
//                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("AddScheduleActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("AddScheduleActivity", t.getMessage());
            }
        });
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.rl_start: {
                timePickerStart.show();
                break;
            }
            case R.id.rl_end: {
                timePickerEnd.show();
                break;
            }
            case R.id.rl_alarm: {
                setExpandable(true);
                break;
            }
            case R.id.rl_share: {
                setExpandable(false);
                break;
            }
            case R.id.btn_add: {
//                sendScheduleInfo();
                finish();
                break;
            }
        }
    };

}
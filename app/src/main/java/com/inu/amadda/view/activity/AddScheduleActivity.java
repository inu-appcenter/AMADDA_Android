package com.inu.amadda.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.inu.amadda.R;
import com.inu.amadda.adapter.GroupChoiceAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.util.Utils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity implements GroupChoiceAdapter.OnSelectListener {

    private boolean isPersonal, isExpanded = false, isAlarmClicked;
    private String startDate = null, endDate = null, color = null, pickColor = null;
    private List<ShareGroup> groupList = new ArrayList<>();
    private int share = -1;

    private AppDatabase appDatabase;

    private ExpandableLayout expandable_alarm, expandable_share;
    private TextView tv_start_date, tv_start_ampm, tv_start_time, tv_end_date, tv_end_ampm, tv_end_time, tv_color, tv_share;
    private TimePickerView timePickerStart, timePickerEnd;
    private EditText et_name, et_place, et_memo;
    private View view_color_tag, view_group_tag;
    private ColorPicker colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        appDatabase = AppDatabase.getInstance(this);

        initialize();
        checkType();
        setToolbar();
        setDefaultDateTime();
        setDateTimePicker();
        setAlarmWheelView();

    }

    @Override
    public void onSelect(ShareGroup group) {
        share = group.getShare();

        view_group_tag.setVisibility(View.VISIBLE);
        view_group_tag.setBackgroundColor(Color.parseColor(group.getColor()));

        tv_share.setTextColor(ContextCompat.getColor(this, R.color.color_6a6a6a));
        tv_share.setText(group.getGroup_name());
    }

    private void checkType() {
        Intent intent = getIntent();
        isPersonal = intent.getBooleanExtra("isPersonal", true);

        LinearLayout ll_color = findViewById(R.id.ll_color);
        LinearLayout ll_share = findViewById(R.id.ll_share);
        if (isPersonal){
            ll_share.setVisibility(View.GONE);
            setPersonalColor();
        }
        else {
            ll_color.setVisibility(View.GONE);
            setRecyclerView();
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

    private void setPersonalColor() {
        color = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.COLOR, null);

        if (color != null){
            view_color_tag.setBackgroundColor(Color.parseColor(color));
            view_color_tag.setVisibility(View.VISIBLE);
            tv_color.setTextColor(ContextCompat.getColor(AddScheduleActivity.this, R.color.color_6a6a6a));
            tv_color.setText(String.valueOf(color));
        }
    }

    private void setColorPicker() {
        colorPicker = new ColorPicker(this);
        colorPicker
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int colorChoice) {
                if (colorChoice != 0){
                    view_color_tag.setBackgroundColor(colorChoice);
                    view_color_tag.setVisibility(View.VISIBLE);
                    tv_color.setTextColor(ContextCompat.getColor(AddScheduleActivity.this, R.color.color_6a6a6a));
                    pickColor = Utils.toStringColor(colorChoice);
                    tv_color.setText(pickColor);
                }
                ((ViewGroup) colorPicker.getDialogViewLayout().getParent()).removeView(colorPicker.getDialogViewLayout());
            }

            @Override
            public void onCancel(){
                ((ViewGroup) colorPicker.getDialogViewLayout().getParent()).removeView(colorPicker.getDialogViewLayout());
            }
        })
                .setTitle("컬러 선택")
                .setColors(R.array.color_picker_array)
                .setRoundColorButton(true);
        if (color != null)
            colorPicker.setDefaultColorButton(Color.parseColor(color));
        colorPicker.show();
    }

    private void setRecyclerView() {
        new Thread(() -> {
            groupList.addAll(appDatabase.groupDao().getList());
        }).start();

        RecyclerView recyclerView = findViewById(R.id.rv_shared_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupChoiceAdapter adapter = new GroupChoiceAdapter(groupList, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void initialize() {
        et_name = findViewById(R.id.et_name);

        RelativeLayout rl_start = findViewById(R.id.rl_start);
        rl_start.setOnClickListener(onClickListener);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_start_ampm = findViewById(R.id.tv_start_ampm);
        tv_start_time = findViewById(R.id.tv_start_time);

        RelativeLayout rl_end = findViewById(R.id.rl_end);
        rl_end.setOnClickListener(onClickListener);
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_end_ampm = findViewById(R.id.tv_end_ampm);
        tv_end_time = findViewById(R.id.tv_end_time);

        et_place = findViewById(R.id.et_place);

        RelativeLayout rl_alarm = findViewById(R.id.rl_alarm);
        rl_alarm.setOnClickListener(onClickListener);
        expandable_alarm = findViewById(R.id.expandable_alarm);

        RelativeLayout rl_color = findViewById(R.id.rl_color);
        rl_color.setOnClickListener(onClickListener);
        view_color_tag = findViewById(R.id.view_color_tag);
        tv_color = findViewById(R.id.tv_color);

        RelativeLayout rl_share = findViewById(R.id.rl_share);
        rl_share.setOnClickListener(onClickListener);
        view_group_tag = findViewById(R.id.view_group_tag);
        tv_share = findViewById(R.id.tv_share);

        expandable_share = findViewById(R.id.expandable_share);

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
            startDate = new SimpleDateFormat(DateUtils.dateFormat, Locale.getDefault()).format(date);
        }
        else {
            endDate = new SimpleDateFormat(DateUtils.dateFormat, Locale.getDefault()).format(date);
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
        addScheduleModel.setSchedule_name(et_name.getText().toString());
        addScheduleModel.setStart(startDate);
        addScheduleModel.setEnd(endDate);
        addScheduleModel.setLocation(et_place.getText().toString());
//        addScheduleModel.setAlarm(null);
        if (!isPersonal){
            addScheduleModel.setShare(share);
        }
        addScheduleModel.setMemo(et_memo.getText().toString());

        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        RetrofitInstance.getInstance().getService().AddSchedule(token, addScheduleModel).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            if (isPersonal && pickColor != null){
                                PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.COLOR, pickColor);
                            }
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
            case R.id.rl_color: {
                setColorPicker();
                break;
            }
            case R.id.rl_share: {
                setExpandable(false);
                break;
            }
            case R.id.btn_add: {
                if (isPersonal){
                    if (color == null && pickColor == null){
                        Toast.makeText(getApplicationContext(), "컬러를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendScheduleInfo();
                    }
                }
                else {
                    if (share == -1){
                        Toast.makeText(getApplicationContext(), "공유할 그룹을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendScheduleInfo();
                    }
                }
                break;
            }
        }
    };

}

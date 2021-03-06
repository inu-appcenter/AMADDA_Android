package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ClassData;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.Time;
import com.inu.amadda.timetable.TimetableView;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class EditClassActivity extends AppCompatActivity {

    private boolean isExpanded = false, isStartClicked;
    private String startDay = "월", startAmpm = "오전", startHour = "01", startMinute = "00", endDay = "월", endAmpm = "오전", endHour = "01", endMinute = "00";
    private int idx;

    private Schedule schedule;

    private EditText et_class_name, et_place;
    private TextView tv_start_day, tv_start_ampm, tv_start_time, tv_end_day, tv_end_ampm, tv_end_time;
    private ExpandableLayout expandable_start, expandable_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        initialize();
        getInitialData();
        setToolbar();
        setDayTimePicker();

    }

    private void initialize() {
        et_class_name = findViewById(R.id.et_class_name);

        RelativeLayout rl_start = findViewById(R.id.rl_start);
        rl_start.setOnClickListener(onClickListener);
        tv_start_day = findViewById(R.id.tv_start_day);
        tv_start_day.setText(startDay);
        tv_start_ampm = findViewById(R.id.tv_start_ampm);
        tv_start_ampm.setText(startAmpm);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_time.setText(startHour + ":" + startMinute);

        expandable_start = findViewById(R.id.expandable_start);

        RelativeLayout rl_end = findViewById(R.id.rl_end);
        rl_end.setOnClickListener(onClickListener);
        tv_end_day = findViewById(R.id.tv_end_day);
        tv_end_day.setText(endDay);
        tv_end_ampm = findViewById(R.id.tv_end_ampm);
        tv_end_ampm.setText(endAmpm);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_time.setText(endHour + ":" + endMinute);

        expandable_end = findViewById(R.id.expandable_end);

        et_place = findViewById(R.id.et_place);

        TextView btn_add = findViewById(R.id.btn_add);
        btn_add.setText("수정 완료");
        btn_add.setOnClickListener(onClickListener);
    }

    private void getInitialData() {
        Intent intent = getIntent();
        schedule = (Schedule) intent.getSerializableExtra("class");
        idx = intent.getIntExtra("idx", -1);

        if (schedule != null) {
            et_class_name.setText(schedule.getClassTitle());

            switch (schedule.getDay()) {
                case DateUtils.MON: {
                    startDay = "월";
                    endDay = "월";
                    break;
                }
                case DateUtils.TUE: {
                    startDay = "화";
                    endDay = "화";
                    break;
                }
                case DateUtils.WED: {
                    startDay = "수";
                    endDay = "수";
                    break;
                }
                case DateUtils.THU: {
                    startDay = "목";
                    endDay = "목";
                    break;
                }
                case DateUtils.FRI: {
                    startDay = "금";
                    endDay = "금";
                    break;
                }
                default:
                    break;
            }
            tv_start_day.setText(startDay);
            tv_end_day.setText(endDay);

            String startData = formatIntegerZero(schedule.getStartTime().getHour(), schedule.getStartTime().getMinute());
            LocalTime startLocalTime = LocalTime.parse(startData, DateTimeFormatter.ofPattern("HHmm"));
            startAmpm = startLocalTime.format(DateTimeFormatter.ofPattern("a", Locale.KOREAN));
            startHour = startLocalTime.format(DateTimeFormatter.ofPattern("hh", Locale.KOREAN));
            startMinute = startLocalTime.format(DateTimeFormatter.ofPattern("mm", Locale.KOREAN));
            tv_start_ampm.setText(startAmpm);
            tv_start_time.setText(startHour + ":" + startMinute);

            String endData = formatIntegerZero(schedule.getEndTime().getHour(), schedule.getEndTime().getMinute());
            LocalTime endLocalTime = LocalTime.parse(endData, DateTimeFormatter.ofPattern("HHmm"));
            endAmpm = endLocalTime.format(DateTimeFormatter.ofPattern("a", Locale.KOREAN));
            endHour = endLocalTime.format(DateTimeFormatter.ofPattern("hh", Locale.KOREAN));
            endMinute = endLocalTime.format(DateTimeFormatter.ofPattern("mm", Locale.KOREAN));
            tv_end_ampm.setText(endAmpm);
            tv_end_time.setText(endHour + ":" + endMinute);

            et_place.setText(schedule.getClassPlace());
        }
    }

    private String formatIntegerZero(int hour, int minute){
        return String.format("%02d%02d", hour, minute);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.back);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn_image.setVisibility(View.GONE);

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setText("삭제");
        right_btn_text.setOnClickListener(onClickListener);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("수업 세부사항");
    }

    private void setDayTimePicker() {
        int color_1eb1fc = ContextCompat.getColor(this, R.color.color_1eb1fc);
        int color_cccccc = ContextCompat.getColor(this, R.color.color_cccccc);

        ArrayList<String> options1Items = new ArrayList<>();
        options1Items.add("월");
        options1Items.add("화");
        options1Items.add("수");
        options1Items.add("목");
        options1Items.add("금");

        ArrayList<String> options2Items = new ArrayList<>();
        options2Items.add("오전");
        options2Items.add("오후");

        ArrayList<String> options3Items = new ArrayList<>();
        options3Items.add("01");
        options3Items.add("02");
        options3Items.add("03");
        options3Items.add("04");
        options3Items.add("05");
        options3Items.add("06");
        options3Items.add("07");
        options3Items.add("08");
        options3Items.add("09");
        options3Items.add("10");
        options3Items.add("11");
        options3Items.add("12");

        ArrayList<String> options4Items = new ArrayList<>();
        options4Items.add("00");
        options4Items.add("05");
        options4Items.add("10");
        options4Items.add("15");
        options4Items.add("20");
        options4Items.add("25");
        options4Items.add("30");
        options4Items.add("35");
        options4Items.add("40");
        options4Items.add("45");
        options4Items.add("50");
        options4Items.add("55");

        WheelView wheel_start_day = findViewById(R.id.wheel_start_day);
        wheel_start_day.setItemsVisibleCount(3);
        wheel_start_day.setTextColorCenter(color_1eb1fc);
        wheel_start_day.setTextColorOut(color_cccccc);
        wheel_start_day.setTextSize(20);
        wheel_start_day.setLineSpacingMultiplier(3);
        wheel_start_day.setDividerColor(color_1eb1fc);
        wheel_start_day.setCyclic(false);

        wheel_start_day.setAdapter(new ArrayWheelAdapter(options1Items));
        wheel_start_day.setOnItemSelectedListener(index -> {
            startDay = options1Items.get(index);
            tv_start_day.setText(startDay);
        });

        WheelView wheel_start_ampm = findViewById(R.id.wheel_start_ampm);
        wheel_start_ampm.setItemsVisibleCount(3);
        wheel_start_ampm.setTextColorCenter(color_1eb1fc);
        wheel_start_ampm.setTextColorOut(color_cccccc);
        wheel_start_ampm.setTextSize(20);
        wheel_start_ampm.setLineSpacingMultiplier(3);
        wheel_start_ampm.setDividerColor(color_1eb1fc);
        wheel_start_ampm.setCyclic(false);

        wheel_start_ampm.setAdapter(new ArrayWheelAdapter(options2Items));
        wheel_start_ampm.setOnItemSelectedListener(index -> {
            startAmpm = options2Items.get(index);
            tv_start_ampm.setText(startAmpm);
        });

        WheelView wheel_start_hour = findViewById(R.id.wheel_start_hour);
        wheel_start_hour.setItemsVisibleCount(3);
        wheel_start_hour.setTextColorCenter(color_1eb1fc);
        wheel_start_hour.setTextColorOut(color_cccccc);
        wheel_start_hour.setTextSize(20);
        wheel_start_hour.setLineSpacingMultiplier(3);
        wheel_start_hour.setDividerColor(color_1eb1fc);
        wheel_start_hour.setCyclic(false);

        wheel_start_hour.setAdapter(new ArrayWheelAdapter(options3Items));
        wheel_start_hour.setOnItemSelectedListener(index -> {
            startHour = options3Items.get(index);
            tv_start_time.setText(startHour + ":" + startMinute);
        });

        WheelView wheel_start_minute = findViewById(R.id.wheel_start_minute);
        wheel_start_minute.setItemsVisibleCount(3);
        wheel_start_minute.setTextColorCenter(color_1eb1fc);
        wheel_start_minute.setTextColorOut(color_cccccc);
        wheel_start_minute.setTextSize(20);
        wheel_start_minute.setLineSpacingMultiplier(3);
        wheel_start_minute.setDividerColor(color_1eb1fc);
        wheel_start_minute.setCyclic(false);

        wheel_start_minute.setAdapter(new ArrayWheelAdapter(options4Items));
        wheel_start_minute.setOnItemSelectedListener(index -> {
            startMinute = options4Items.get(index);
            tv_start_time.setText(startHour + ":" + startMinute);
        });

        WheelView wheel_end_day = findViewById(R.id.wheel_end_day);
        wheel_end_day.setItemsVisibleCount(3);
        wheel_end_day.setTextColorCenter(color_1eb1fc);
        wheel_end_day.setTextColorOut(color_cccccc);
        wheel_end_day.setTextSize(20);
        wheel_end_day.setLineSpacingMultiplier(3);
        wheel_end_day.setDividerColor(color_1eb1fc);
        wheel_end_day.setCyclic(false);

        wheel_end_day.setAdapter(new ArrayWheelAdapter(options1Items));
        wheel_end_day.setOnItemSelectedListener(index -> {
            endDay = options1Items.get(index);
            tv_end_day.setText(endDay);
        });

        WheelView wheel_end_ampm = findViewById(R.id.wheel_end_ampm);
        wheel_end_ampm.setItemsVisibleCount(3);
        wheel_end_ampm.setTextColorCenter(color_1eb1fc);
        wheel_end_ampm.setTextColorOut(color_cccccc);
        wheel_end_ampm.setTextSize(20);
        wheel_end_ampm.setLineSpacingMultiplier(3);
        wheel_end_ampm.setDividerColor(color_1eb1fc);
        wheel_end_ampm.setCyclic(false);

        wheel_end_ampm.setAdapter(new ArrayWheelAdapter(options2Items));
        wheel_end_ampm.setOnItemSelectedListener(index -> {
            endAmpm = options2Items.get(index);
            tv_end_ampm.setText(endAmpm);
        });

        WheelView wheel_end_hour = findViewById(R.id.wheel_end_hour);
        wheel_end_hour.setItemsVisibleCount(3);
        wheel_end_hour.setTextColorCenter(color_1eb1fc);
        wheel_end_hour.setTextColorOut(color_cccccc);
        wheel_end_hour.setTextSize(20);
        wheel_end_hour.setLineSpacingMultiplier(3);
        wheel_end_hour.setDividerColor(color_1eb1fc);
        wheel_end_hour.setCyclic(false);

        wheel_end_hour.setAdapter(new ArrayWheelAdapter(options3Items));
        wheel_end_hour.setOnItemSelectedListener(index -> {
            endHour = options3Items.get(index);
            tv_end_time.setText(endHour + ":" + endMinute);
        });

        WheelView wheel_end_minute = findViewById(R.id.wheel_end_minute);
        wheel_end_minute.setItemsVisibleCount(3);
        wheel_end_minute.setTextColorCenter(color_1eb1fc);
        wheel_end_minute.setTextColorOut(color_cccccc);
        wheel_end_minute.setTextSize(20);
        wheel_end_minute.setLineSpacingMultiplier(3);
        wheel_end_minute.setDividerColor(color_1eb1fc);
        wheel_end_minute.setCyclic(false);

        wheel_end_minute.setAdapter(new ArrayWheelAdapter(options4Items));
        wheel_end_minute.setOnItemSelectedListener(index -> {
            endMinute = options4Items.get(index);
            tv_end_time.setText(endHour + ":" + endMinute);
        });

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

    private void saveClass() {
        TimetableView timetableView = new TimetableView(this);
        timetableView.loadOnlyData(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, null));

        if (idx != -1) {
            ArrayList<Schedule> schedules = new ArrayList<>();

            Schedule schedule = new Schedule();

            schedule.setClassTitle(et_class_name.getText().toString());
            schedule.setClassPlace(et_place.getText().toString());
            schedule.setProfessorName(this.schedule.getProfessorName());

            String start = startAmpm.equals("오전") ? "AM" + " " + startHour : "PM" + " " + startHour;
            String end = endAmpm.equals("오전") ? "AM" + " " + endHour : "PM" + " " + endHour;
            schedule.setStartTime(new Time(Integer.parseInt(LocalTime.parse(start, DateTimeFormatter.ofPattern("a hh")).format(DateTimeFormatter.ofPattern("HH", Locale.KOREAN))),
                    Integer.parseInt(startMinute)));
            schedule.setEndTime(new Time(Integer.parseInt(LocalTime.parse(end, DateTimeFormatter.ofPattern("a hh")).format(DateTimeFormatter.ofPattern("HH", Locale.KOREAN))),
                    Integer.parseInt(endMinute)));

            switch (startDay) {
                case "월":
                    schedule.setDay(DateUtils.MON);
                    break;
                case "화":
                    schedule.setDay(DateUtils.TUE);
                    break;
                case "수":
                    schedule.setDay(DateUtils.WED);
                    break;
                case "목":
                    schedule.setDay(DateUtils.THU);
                    break;
                case "금":
                    schedule.setDay(DateUtils.FRI);
                    break;
                default:
                    break;
            }

            schedules.add(schedule);

            timetableView.edit(idx, schedules);
            PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, timetableView.createSaveData());

            finish();
        }
    }

    private void deleteClass() {
        TimetableView timetableView = new TimetableView(this);
        timetableView.loadOnlyData(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, null));

        if (idx != -1) {
            timetableView.remove(idx);
            PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, timetableView.createSaveData());

            finish();
        }
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.toolbar_right_text:{
                deleteClass();
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
                saveClass();
                break;
            }
        }
    };

}
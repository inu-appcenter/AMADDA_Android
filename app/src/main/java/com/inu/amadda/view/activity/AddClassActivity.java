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
import com.inu.amadda.model.ClassData;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class AddClassActivity extends AppCompatActivity {

    private static int RESULT = 1000;

    private boolean isExpanded = false, isStartClicked;
    private String startDay = "월", startAmpm = "오전", startHour = "01", startMinute = "00", endDay = "월", endAmpm = "오전", endHour = "01", endMinute = "00";

    private EditText et_class_name, et_place;
    private TextView tv_start_day, tv_start_ampm, tv_start_time, tv_end_day, tv_end_ampm, tv_end_time;
    private ExpandableLayout expandable_start, expandable_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        initialize();
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

    private void sendClassInfo() {
        ClassData data = new ClassData(et_class_name.getText().toString(), "", startDay,
                toFormatTime(startAmpm, startHour, startMinute), toFormatTime(endAmpm, endHour, endMinute), et_place.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("Class", data);
        setResult(RESULT, intent);
        finish();
    }

    private String toFormatTime(String ampm, String hour, String minute) {
        String result;
        if (ampm.equals("오전")){
            if (hour.equals("12")){
                result = "00" + ":" + minute;
            }
            else  {
                result = hour + ":" + minute;
            }
        }
        else {
            if (hour.equals("12")){
                result = hour + ":" + minute;
            }
            else  {
                result = (Integer.valueOf(endHour) + 12) + ":" + minute;
            }
        }
        return result;
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
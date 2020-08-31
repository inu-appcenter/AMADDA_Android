package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.adapter.DayScheduleAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.DaySchedule;
import com.inu.amadda.model.ScheduleData;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.TimetableView;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayScheduleActivity extends AppCompatActivity {

    private List<ScheduleData> scheduleList = new ArrayList<>();
    private List<DaySchedule> itemList = new ArrayList<>();
    private String titleString;
    private LocalDate resultDate;
    private int day;

    private AppDatabase appDatabase;

    private DayScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        appDatabase = AppDatabase.getInstance(this);

        initialize();
        checkType();
        setRecyclerView();
        getSchedulesData();

    }

    private void initialize() {
        CardView cv_add_schedule = findViewById(R.id.cv_add_schedule);
        cv_add_schedule.setOnClickListener(onClickListener);
    }

    private void checkType() {
        Intent intent = getIntent();
        day = intent.getIntExtra("Day", -1);

        if (day != -1) {
            int today = DateUtils.getDayOfWeek();
            LocalDate todayDate = LocalDate.now();
            if (today == DateUtils.SUN){
                resultDate = todayDate.plusDays(day + 1);
            }
            else {
                resultDate = todayDate.plusDays(day - today);
            }
            titleString = DateUtils.getTitleString(resultDate);
        }

        setToolbar();
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
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(titleString);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_schedule_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DayScheduleAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    private void getSchedulesData(){
        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);
        String date = resultDate.format(DateTimeFormatter.ofPattern(DateUtils.dateFormat));

        RetrofitInstance.getInstance().getService().GetDaySchedule(token, date).enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse != null) {
                        if (scheduleResponse.success) {
                            scheduleList.addAll(scheduleResponse.schedules);
                            getGroupColor();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("DayScheduleActivity", scheduleResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("DayScheduleActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("DayScheduleActivity", t.getMessage());
            }
        });
    }

    private void getGroupColor() {
        new Thread(() -> {
            for (int i = 0; i < scheduleList.size(); i++){
                ScheduleData data = scheduleList.get(i);
                String color;
                if (data.getShare() > 0) {
                    color = appDatabase.groupDao().getColorByKey(data.getShare());
                }
                else {
                    color = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.COLOR, null);
                }
                itemList.add(new DaySchedule(true, data.getNumber(), data.getShare(), data.getSchedule_name(), formatScheduleTime(data.getStart()), formatScheduleTime(data.getEnd()),
                        data.getLocation(), data.getMemo(), color, null));
            }

            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                getClassesData();
            });

        }).start();
    }

    private String formatScheduleTime(String string){
        LocalTime localTime = LocalTime.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateTimeFormat));
        return localTime.format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN));
    }

    private String formatClassTime(String string){
        LocalTime localTime = LocalTime.parse(string, DateTimeFormatter.ofPattern("HHmm"));
        return localTime.format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN));
    }

    private String formatIntegerZero(int hour, int minute){
        return String.format("%02d%02d", hour, minute);
    }

    private void getClassesData() {
        TimetableView timetableView = new TimetableView(this);
        timetableView.loadOnlyData(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, null));

        List<Schedule> classList = timetableView.getAllSchedulesInStickers();
        for (int i = 0; i < classList.size(); i++){
            Schedule data = classList.get(i);
            if (data.getDay() == day) {
                itemList.add(new DaySchedule(false, i, -1, data.getClassTitle(),
                        formatClassTime(formatIntegerZero(data.getStartTime().getHour(), data.getStartTime().getMinute())),
                        formatClassTime(formatIntegerZero(data.getEndTime().getHour(), data.getEndTime().getMinute())),
                        data.getClassPlace(), null, null, data));
            }
        }

        Collections.sort(itemList);

        adapter.notifyDataSetChanged();
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.cv_add_schedule: {
                Intent intent = new Intent(this, AddScheduleActivity.class);
                intent.putExtra("isPersonal", true);
                startActivity(intent);
                break;
            }
        }
    };

}

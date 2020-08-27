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
import com.inu.amadda.adapter.ScheduleListAdapter;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ScheduleData;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayScheduleActivity extends AppCompatActivity {

    private List<ScheduleData> scheduleList = new ArrayList<>();
    private String titleString;
    private LocalDate resultDate;

    private ScheduleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

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
        int day = intent.getIntExtra("Day", -1);

        if (day != -1) {
            int today = DateUtils.getDayOfWeek();
            LocalDate todayDate = LocalDate.now();
            resultDate = todayDate.plusDays(day - today);
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

        adapter = new ScheduleListAdapter(scheduleList);
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
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("DayScheduleListActivity", scheduleResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("DayScheduleListActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("DayScheduleListActivity", t.getMessage());
            }
        });
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

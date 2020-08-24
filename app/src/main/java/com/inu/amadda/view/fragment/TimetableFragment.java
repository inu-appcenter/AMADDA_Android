package com.inu.amadda.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ScheduleData;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.Time;
import com.inu.amadda.timetable.TimetableView;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableFragment extends Fragment {

    private TimetableView timetable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        getSchedulesData();
        getTimetableData(view);

        return view;
    }

    private void getSchedulesData(){
        String token = PreferenceManager.getInstance().getSharedPreference(getContext(), Constant.Preference.TOKEN, null);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        RetrofitInstance.getInstance().getService().GetWeekSchedule(token, date).enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse != null) {
                        if (scheduleResponse.success) {
                            setScheduleSticker(scheduleResponse.schedules);
                        }
                        else {
                            Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("TimetableFragment", scheduleResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("TimetableFragment", status + "");
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("TimetableFragment", t.getMessage());
            }
        });

    }

    private void getTimetableData(View view) {
        timetable = view.findViewById(R.id.timetable);
        timetable.setDayHighlight();

        String data = PreferenceManager.getInstance().getSharedPreference(getContext(), Constant.Preference.TIMETABLE, null);
        if (data == null){
            timetable.setVisibility(View.INVISIBLE);
            LinearLayout ll_blank = view.findViewById(R.id.ll_blank);
            ll_blank.setVisibility(View.VISIBLE);
        }
        else {
            timetable.load(data);
        }
    }

    private void setScheduleSticker(List<ScheduleData> data) {
        for(int i = 0; i < data.size(); i++){
            if (!data.get(i).getStart().equals("Invalid date") && !data.get(i).getEnd().equals("Invalid date")){
                LocalDate startDate = StringToLocalDate(data.get(i).getStart());
                LocalDate endDate = StringToLocalDate(data.get(i).getEnd());
                DayOfWeek day = startDate.getDayOfWeek();

                if (startDate.isEqual(endDate) && day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY){
                    ArrayList<Schedule> schedules = new ArrayList<>();
                    Schedule schedule = new Schedule();

                    schedule.setClassTitle(data.get(i).getSchedule_name());
                    schedule.setClassPlace(data.get(i).getLocation());
                    Log.d("TimetableFragment", data.get(i).getNumber() + " " + data.get(i).getSchedule_name());

                    LocalTime startTime = StringToLocalTime(data.get(i).getStart());
                    LocalTime endTime = StringToLocalTime(data.get(i).getEnd());
                    schedule.setStartTime(new Time(startTime.getHour(), startTime.getMinute()));
                    schedule.setEndTime(new Time(endTime.getHour(), endTime.getMinute()));
                    Log.d("TimetableFragment", startTime.getHour() + " " + startTime.getMinute());
                    Log.d("TimetableFragment", endTime.getHour() + " " + endTime.getMinute());

                    switch (day){
                        case MONDAY:
                            schedule.setDay(0);
                            break;
                        case TUESDAY:
                            schedule.setDay(1);
                            break;
                        case WEDNESDAY:
                            schedule.setDay(2);
                            break;
                        case THURSDAY:
                            schedule.setDay(3);
                            break;
                        case FRIDAY:
                            schedule.setDay(4);
                            break;
                    }

                    schedules.add(schedule);
                    timetable.addSchedule(schedules, data.get(i).getShare(), getActivity());
                }
            }

        }
    }

    private LocalDate StringToLocalDate(String string){
        return LocalDate.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateFormat));
    }

    private LocalTime StringToLocalTime(String string){
        return LocalTime.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateFormat));
    }

}

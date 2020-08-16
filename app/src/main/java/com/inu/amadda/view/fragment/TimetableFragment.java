package com.inu.amadda.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        timetable = view.findViewById(R.id.timetable);

        setClassSticker();

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

    private void setClassSticker() {
        ArrayList<Schedule> schedules3 = new ArrayList<Schedule>();

        Schedule schedule3 = new Schedule();
        schedule3.setClassTitle("아이덴티티디자인"); // sets subject
        schedule3.setClassPlace("16호관 213호"); // sets place
        schedule3.setProfessorName("Won Kim"); // sets professor
        schedule3.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule3.setEndTime(new Time(14,50)); // sets the end of class time (hour,minute)
        schedule3.setDay(0);
        schedules3.add(schedule3);

        timetable.addClass(schedules3);


        ArrayList<Schedule> schedules1 = new ArrayList<Schedule>();

        Schedule schedule1 = new Schedule();
        schedule1.setClassTitle("광고론"); // sets subject
        schedule1.setClassPlace("28호관240호"); // sets place
        schedule1.setProfessorName("Won Ki22m"); // sets professor
        schedule1.setStartTime(new Time(15,0)); // sets the beginning of class time (hour,minute)
        schedule1.setEndTime(new Time(16,45)); // sets the end of class time (hour,minute)
        schedule1.setDay(0);
        schedules1.add(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setClassTitle("광고론"); // sets subject
        schedule2.setClassPlace("28호관240호"); // sets place
        schedule2.setProfessorName("Won Ki22m"); // sets professor
        schedule2.setStartTime(new Time(13,30)); // sets the beginning of class time (hour,minute)
        schedule2.setEndTime(new Time(14,45)); // sets the end of class time (hour,minute)
        schedule2.setDay(1);
        schedules1.add(schedule2);

        timetable.addClass(schedules1);


        ArrayList<Schedule> schedules2 = new ArrayList<Schedule>();

        Schedule schedule5 = new Schedule();
        schedule5.setClassTitle("디자인 스튜디오 2 - 협력 프로젝트"); // sets subject
        schedule5.setClassPlace("28호관204호"); // sets place
        schedule5.setProfessorName("Won Ki22m"); // sets professor
        schedule5.setStartTime(new Time(18,0)); // sets the beginning of class time (hour,minute)
        schedule5.setEndTime(new Time(20,50)); // sets the end of class time (hour,minute)
        schedule5.setDay(3);
        schedules2.add(schedule5);

        Schedule schedule6 = new Schedule();
        schedule6.setClassTitle("디자인 스튜디오 2 - 협력 프로젝트"); // sets subject
        schedule6.setClassPlace("28호관204호"); // sets place
        schedule6.setProfessorName("Won Ki22m"); // sets professor
        schedule6.setStartTime(new Time(10,0)); // sets the beginning of class time (hour,minute)
        schedule6.setEndTime(new Time(12,50)); // sets the end of class time (hour,minute)
        schedule6.setDay(2);
        schedules2.add(schedule6);

        timetable.addClass(schedules2);


    }

    private void setScheduleSticker(List<ScheduleData> data) {
        for(int i = 0; i < data.size(); i++){
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

    private LocalDate StringToLocalDate(String string){
        return LocalDate.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateFormat));
    }

    private LocalTime StringToLocalTime(String string){
        return LocalTime.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateFormat));
    }

}

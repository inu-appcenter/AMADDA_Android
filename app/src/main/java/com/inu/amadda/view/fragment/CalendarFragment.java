package com.inu.amadda.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.inu.amadda.R;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ScheduleData;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private List<ScheduleData> schedules = new ArrayList<>();

    private AppDatabase appDatabase;

    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        appDatabase = AppDatabase.getInstance(getActivity());

        getSchedulesData();
        setCalendarView(view);

        return view;
    }

    private void setCalendarView(View view) {
        calendarView = view.findViewById(R.id.calendarView);

        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(2); // 라이브러리 버그
        YearMonth lastMonth = currentMonth.plusMonths(4); // 라이브러리 버그
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        calendarView.setDayWidth((int)(metrics.widthPixels - 2 * 18 * metrics.density) / 7 + 1);
        calendarView.setDayHeight((int)(68 * metrics.density));

        calendarView.setDayBinder(dayBinder);
        calendarView.setMonthHeaderBinder(monthHeaderBinder);
    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void getSchedulesData(){
        String token = PreferenceManager.getInstance().getSharedPreference(getContext(), Constant.Preference.TOKEN, null);

        RetrofitInstance.getInstance().getService().GetAllSchedule(token).enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse != null) {
                        if (scheduleResponse.success) {
                            schedules.clear();
                            schedules.addAll(scheduleResponse.schedules);
                            calendarView.notifyCalendarChanged();
                        }
                        else {
                            Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("CalendarFragment", scheduleResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "일정을 불러올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("CalendarFragment", status + "");
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("CalendarFragment", t.getMessage());
            }
        });

    }

    private DayBinder dayBinder = new DayBinder<DayViewContainer>() {
        @NonNull
        @Override
        public DayViewContainer create(@NonNull View view) {
            return new DayViewContainer(view);
        }

        @Override
        public void bind(@NonNull DayViewContainer container,@NonNull CalendarDay day) {
            //일요일은 빨간색으로 표시
            if (day.getDate().getDayOfWeek() == DayOfWeek.SUNDAY){
                container.tv_calendar_day.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_ff0000));
            }

            LocalDate currentDay = day.getDate();
            Log.d("CalendarFragment", "current: " + currentDay.toString());
            List<ScheduleData> currentSchedules = new ArrayList<>();

            //이번 달 날짜만 표시
            if (day.getOwner() == DayOwner.THIS_MONTH){
                container.tv_calendar_day.setVisibility(View.VISIBLE);
                container.tv_calendar_day.setText(String.valueOf(currentDay.getDayOfMonth()));

                //오늘 날짜 표시
                if (currentDay.equals(LocalDate.now())){
                    container.tv_today.setText(String.valueOf(currentDay.getDayOfMonth()));
                    container.tv_today.setVisibility(View.VISIBLE);
                }
//                else {
//                    container.tv_today.setVisibility(View.GONE);
//                }

                //오늘 이벤트만 저장
                for (int i = 0; i < schedules.size(); i++) {
                    LocalDate startDay = StringToLocalDate(schedules.get(i).getStart());
                    LocalDate endDay = StringToLocalDate(schedules.get(i).getEnd());
                    if (currentDay.isEqual(startDay) || (currentDay.isAfter(startDay) && currentDay.isBefore(endDay)) || currentDay.isEqual(endDay)){
                        currentSchedules.add(schedules.get(i));
                    }
                }
            }
            else {
                container.tv_calendar_day.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i < currentSchedules.size(); i++){
                Log.d("CalendarFragment", "num: " + currentSchedules.get(i).getNumber());
                LocalDate startDay = StringToLocalDate(currentSchedules.get(i).getStart());
                LocalDate endDay = StringToLocalDate(currentSchedules.get(i).getEnd());
                Log.d("CalendarFragment", "start: " + startDay.toString());
                Log.d("CalendarFragment", "end: " + endDay.toString());
                Log.d("CalendarFragment", "share: " + currentSchedules.get(i).getShare());

                int share = currentSchedules.get(i).getShare();
                if (share > 0){
                    new Thread(() -> {
                        String color = appDatabase.groupDao().getColorByKey(share);
                        if (color != null)
                            setScheduleTag(container, startDay, endDay, currentDay, color);
                    }).start();
                }
                else {
                    String color = PreferenceManager.getInstance().getSharedPreference(getActivity(), Constant.Preference.COLOR, null);
                    setScheduleTag(container, startDay, endDay, currentDay, color);
                }
            }

        }
    };

    private void setScheduleTag(DayViewContainer container, LocalDate startDay, LocalDate endDay, LocalDate currentDay, String color) {
        //이벤트 표시 - 하루
        if (startDay.isEqual(endDay) && currentDay.isEqual(startDay)){
            View index = new View(getContext());
            index.setId(View.generateViewId());
            index.setBackgroundColor(Color.parseColor(color));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(8));
            index.setLayoutParams(params);
            params.setMargins(dpToPx(12), 0, dpToPx(12), 0);
            container.rl_calendar_day.addView(index);
        }
        //이벤트 표시 - 연속
        else {
            if (currentDay.isEqual(startDay)){
                View index = new View(getContext());
                index.setId(View.generateViewId());
                index.setBackgroundColor(Color.parseColor(color));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(8));
                index.setLayoutParams(params);
                params.setMargins(dpToPx(12), 0, 0, 0);
                container.rl_calendar_day.addView(index);
            }
            else if (currentDay.isAfter(startDay) && currentDay.isBefore(endDay)){
                View index = new View(getContext());
                index.setId(View.generateViewId());
                index.setBackgroundColor(Color.parseColor(color));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(8));
                index.setLayoutParams(params);
                container.rl_calendar_day.addView(index);
            }
            else if (currentDay.isEqual(endDay)){
                View index = new View(getContext());
                index.setId(View.generateViewId());
                index.setBackgroundColor(Color.parseColor(color));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(8));
                index.setLayoutParams(params);
                params.setMargins(0, 0, dpToPx(12), 0);
                container.rl_calendar_day.addView(index);
            }
        }
    }

    private LocalDate StringToLocalDate(String string){
        return LocalDate.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateFormat));
    }

    private MonthHeaderFooterBinder monthHeaderBinder = new MonthHeaderFooterBinder<MonthHeaderViewContainer>() {
        @NonNull
        @Override
        public MonthHeaderViewContainer create(@NonNull View view) {
            return new MonthHeaderViewContainer(view);
        }

        @Override
        public void bind(@NonNull MonthHeaderViewContainer container,@NonNull CalendarMonth month) {
            container.textView.setText(month.getYear() + "년 " + month.getMonth() + "월");

        }
    };

    class DayViewContainer extends ViewContainer {

        RelativeLayout rl_calendar_day;
        TextView tv_calendar_day, tv_today;

        DayViewContainer(View view) {
            super(view);
            rl_calendar_day = view.findViewById(R.id.rl_calendar_day);
            tv_calendar_day = view.findViewById(R.id.tv_calendar_day);
            tv_today = view.findViewById(R.id.tv_today);
        }
    }

    class MonthHeaderViewContainer extends ViewContainer {

        TextView textView;

        MonthHeaderViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.calendarMonthText);
        }
    }

}

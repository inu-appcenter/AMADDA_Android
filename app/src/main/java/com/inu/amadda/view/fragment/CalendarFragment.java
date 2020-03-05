package com.inu.amadda.view.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.inu.amadda.R;
import com.inu.amadda.model.ScheduleDataModel;
import com.inu.amadda.util.DateUtils;
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
import org.threeten.bp.temporal.WeekFields;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private List<ScheduleDataModel> schedules = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        getSchedulesData();
        setCalendarView(view);

        return view;
    }

    private void setCalendarView(View view) {
        CalendarView calendarView = view.findViewById(R.id.calendarView);

        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(3); // 라이브러리 버그 (2020년 3월 기준, 2019년 11월부터)
        YearMonth lastMonth = currentMonth.plusMonths(5); // 라이브러리 버그 (2020년 3월 기준, 2020년 9월부터)
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        calendarView.setDayWidth((int)(metrics.widthPixels - 2 * 18 * metrics.density) / 7);
        calendarView.setDayHeight((int)(68 * metrics.density));

        calendarView.setDayBinder(dayBinder);
        calendarView.setMonthHeaderBinder(monthHeaderBinder);
    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void getSchedulesData(){
        ScheduleDataModel model = new ScheduleDataModel();
        model.setStart("2020-03-10 00:00:00");
        model.setEnd("2020-03-10 00:00:00");

        ScheduleDataModel model1 = new ScheduleDataModel();
        model1.setStart("2020-03-03 00:00:00");
        model1.setEnd("2020-03-06 00:00:00");

        ScheduleDataModel model2 = new ScheduleDataModel();
        model2.setStart("2020-03-30 00:00:00");
        model2.setEnd("2020-04-02 00:00:00");

        ScheduleDataModel model3 = new ScheduleDataModel();
        model3.setStart("2020-04-30 00:00:00");
        model3.setEnd("2020-05-01 00:00:00");

        schedules.add(model);
        schedules.add(model1);
        schedules.add(model2);
        schedules.add(model3);
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

            //오늘 날짜 표시
            if (day.getDate().equals(LocalDate.now())){
                container.tv_today.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container.tv_today.setVisibility(View.VISIBLE);
            }
            else {
                container.tv_today.setVisibility(View.GONE);
            }

            for (int i = 0; i < schedules.size(); i++){
                int currentMonth = day.getDate().getMonthValue();
                int currentDay = day.getDate().getDayOfMonth();

                int startMonth = getMonthValue(schedules.get(i).getStart());
                int startDay = getDayValue(schedules.get(i).getStart());
                int endMonth = getMonthValue(schedules.get(i).getEnd());
                int endDay = getDayValue(schedules.get(i).getEnd());

                //이벤트 표시 - 하루
                if (startMonth == endMonth && startDay == endDay){
                    if (currentMonth == startMonth && currentDay == startDay){
                        container.index.setVisibility(View.VISIBLE);
                        container.index.setBackgroundResource(R.color.color_ff0000);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                        params.setMargins(dpToPx(12), 0, dpToPx(12), 0);
                    }
                }
                //이벤트 표시 - 연속
                else {
                    //같은 달일 때
                    if (startMonth == endMonth && currentMonth == startMonth){
                        if (currentDay == startDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_22af1d);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                            params.setMargins(dpToPx(12), 0, 0, 0);
                        }
                        else if (currentDay > startDay && currentDay < endDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_22af1d);
                        }
                        else if (currentDay == endDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_22af1d);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                            params.setMargins(0, 0, dpToPx(12), 0);
                        }
                    }
                    //다른 달일 때
                    else if ( (startMonth < endMonth) || (startMonth == 12 && endMonth == 1)){
                        int lastOfStartMonth = getLastDayOfMonth(schedules.get(i).getStart());
                        if (currentMonth == startMonth && currentDay == startDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_ffc915);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                            params.setMargins(dpToPx(12), 0, 0, 0);
                        }
                        else if (currentMonth == startMonth && currentDay > startDay && currentDay <= lastOfStartMonth){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_ffc915);
                        }
                        else if (currentMonth == endMonth && currentDay >= 1 && currentDay < endDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_ffc915);
                        }
                        else if (currentMonth == endMonth && currentDay == endDay){
                            container.index.setVisibility(View.VISIBLE);
                            container.index.setBackgroundResource(R.color.color_ffc915);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                            params.setMargins(0, 0, dpToPx(12), 0);
                        }
                    }
                }
            }

            //이번 달 날짜만 표시
            if (day.getOwner() == DayOwner.THIS_MONTH){
                container.tv_calendar_day.setVisibility(View.VISIBLE);
                container.tv_calendar_day.setText(String.valueOf(day.getDate().getDayOfMonth()));
            }
            else {
                container.tv_calendar_day.setVisibility(View.INVISIBLE);
                container.index.setVisibility(View.GONE);
            }
        }
    };

    private Calendar DateStringToCalendar(String string){
        Date date = null;
        try {
            date = DateUtils.dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private int getMonthValue(String string){
        return DateStringToCalendar(string).get(Calendar.MONTH) + 1;
    }

    private int getDayValue(String string){
        return DateStringToCalendar(string).get(Calendar.DAY_OF_MONTH);
    }

    private int getLastDayOfMonth(String string) {
        return DateStringToCalendar(string).getActualMaximum(Calendar.DAY_OF_MONTH);
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

        TextView tv_calendar_day, tv_today;
        View index;

        DayViewContainer(View view) {
            super(view);
            tv_calendar_day = view.findViewById(R.id.tv_calendar_day);
            tv_today = view.findViewById(R.id.tv_today);
            index = view.findViewById(R.id.view_index);
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

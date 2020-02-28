package com.inu.amadda.view;

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

import java.util.Locale;

public class CalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);

        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(1);
        YearMonth lastMonth = currentMonth.plusMonths(1);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        calendarView.setDayWidth((int)(metrics.widthPixels - 2 * 18 * metrics.density) / 7);
        calendarView.setDayHeight((int)(68 * metrics.density));

        calendarView.setDayBinder(dayBinder);
        calendarView.setMonthHeaderBinder(monthHeaderBinder);

        return view;
    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private DayBinder dayBinder = new DayBinder<DayViewContainer>() {
        @NonNull
        @Override
        public DayViewContainer create(@NonNull View view) {
            return new DayViewContainer(view);
        }

        @Override
        public void bind(@NonNull DayViewContainer container,@NonNull CalendarDay day) {
            //이번 달 날짜만 표시
            if (day.getOwner() == DayOwner.THIS_MONTH){
                container.tv_calendar_day.setText(String.valueOf(day.getDate().getDayOfMonth()));
            }

            //일요일은 빨간색으로 표시
            if (day.getDate().getDayOfWeek() == DayOfWeek.SUNDAY){
                container.tv_calendar_day.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_ff0000));
            }

            //오늘 날짜 표시
            if (day.getDate().equals(LocalDate.now())){
                container.tv_today.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container.tv_today.setVisibility(View.VISIBLE);
            }

            //이벤트 표시 - 하루
            if (day.getDate().getMonthValue() == 2 && day.getDate().getDayOfMonth() == 7){
                container.index.setVisibility(View.VISIBLE);
                container.index.setBackgroundResource(R.color.color_ff0000);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                params.setMargins(dpToPx(12), 0, dpToPx(12), 0);
            }
            else if (day.getDate().getMonthValue() == 2 && day.getDate().getDayOfMonth() == 28){
                container.index.setVisibility(View.VISIBLE);
                container.index.setBackgroundResource(R.color.color_22af1d);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                params.setMargins(dpToPx(12), 0, dpToPx(12), 0);
            }

            //이벤트 표시 - 연속
            if (day.getDate().getMonthValue() == 2 && day.getDate().getDayOfMonth() == 24){
                container.index.setVisibility(View.VISIBLE);
                container.index.setBackgroundResource(R.color.color_8234ff);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                params.setMargins(dpToPx(12), 0, 0, 0);
            }
            else if (day.getDate().getMonthValue() == 2 && day.getDate().getDayOfMonth() == 25){
                container.index.setVisibility(View.VISIBLE);
                container.index.setBackgroundResource(R.color.color_8234ff);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.index.getLayoutParams();
                params.setMargins(0, 0, dpToPx(12), 0);
            }
        }
    };

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

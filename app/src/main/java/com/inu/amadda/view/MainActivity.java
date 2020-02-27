package com.inu.amadda.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inu.amadda.R;
import com.inu.amadda.model.Schedule;
import com.inu.amadda.model.Time;
import com.inu.amadda.util.TodayDate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Animation rotate_forward, rotate_backward;
    private PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setFloatingActionButton();

        TimetableView timetable = findViewById(R.id.timetable);


        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Schedule schedule = new Schedule();
        schedule.setClassTitle("아이덴티티디자인"); // sets subject
        schedule.setClassPlace("16호관 213호"); // sets place
        schedule.setProfessorName("Won Kim"); // sets professor
        schedule.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule.setEndTime(new Time(14,50)); // sets the end of class time (hour,minute)
        schedule.setDay(0);
        schedules.add(schedule);

        timetable.add(schedules);


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

        timetable.add(schedules1);


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

        timetable.add(schedules2);

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.side_menu);
        AppCompatImageButton right_btn = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn.setImageResource(R.drawable.calendar);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(TodayDate.getToday());
    }

    private void setFloatingActionButton() {
        fab = findViewById(R.id.fab_add_schedule);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(onClickListener);
    }

    private void setPopupMenu(View view) {
        popup = new PopupMenu(this, view);
        popup.inflate(R.menu.menu_add_schedule);
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.setOnDismissListener(onDismissListener);
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.fab_add_schedule:{
                if (popup == null)
                    setPopupMenu(view);
                fab.startAnimation(rotate_forward);
                popup.show();
                break;
            }
        }
    };

    PopupMenu.OnMenuItemClickListener onMenuItemClickListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.add_class:

                return true;
            case R.id.personal_schedule:

                return true;
            case R.id.shared_schedule:

                return true;
            default:
                return false;
        }
    };

    PopupMenu.OnDismissListener onDismissListener = popupMenu -> {
        fab.startAnimation(rotate_backward);
    };

}

package com.inu.amadda.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.inu.amadda.R;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.Time;
import com.inu.amadda.timetable.TimetableView;

import java.util.ArrayList;

public class TimetableFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);


        TimetableView timetable = view.findViewById(R.id.timetable);


        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Schedule schedule = new Schedule();
        schedule.setClassTitle("아이덴티티디자인"); // sets subject
        schedule.setClassPlace("16호관 213호"); // sets place
        schedule.setProfessorName("Won Kim"); // sets professor
        schedule.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule.setEndTime(new Time(14,50)); // sets the end of class time (hour,minute)
        schedule.setDay(0);
        schedules.add(schedule);

        timetable.addClass(schedules);


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


        ArrayList<Schedule> scheduless = new ArrayList<Schedule>();

        Schedule schedule4 = new Schedule();
        schedule4.setClassTitle("길동이랑 저녁식사"); // sets subject
        schedule4.setClassPlace("부평"); // sets place
        schedule4.setStartTime(new Time(16,0)); // sets the beginning of class time (hour,minute)
        schedule4.setEndTime(new Time(19,0)); // sets the end of class time (hour,minute)
        schedule4.setDay(1);
        scheduless.add(schedule4);

        Schedule schedule7 = new Schedule();
        schedule7.setClassTitle("길동이랑 저녁식사"); // sets subject
        schedule7.setClassPlace("부평"); // sets place
        schedule7.setStartTime(new Time(16,0)); // sets the beginning of class time (hour,minute)
        schedule7.setEndTime(new Time(19,0)); // sets the end of class time (hour,minute)
        schedule7.setDay(0);
        scheduless.add(schedule7);

        timetable.addSchedule(scheduless);

        return view;
    }

}

package com.inu.amadda.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.inu.amadda.R;
import com.inu.amadda.model.Schedule;
import com.inu.amadda.model.Time;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimetableView timetable = findViewById(R.id.timetable);


        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Schedule schedule = new Schedule();
        schedule.setClassTitle("Data Structure"); // sets subject
        schedule.setClassPlace("IT-601"); // sets place
        schedule.setProfessorName("Won Kim"); // sets professor
        schedule.setStartTime(new Time(10,0)); // sets the beginning of class time (hour,minute)
        schedule.setEndTime(new Time(13,30)); // sets the end of class time (hour,minute)
        schedule.setDay(0);
        schedules.add(schedule);

        Schedule schedule3 = new Schedule();
        schedule3.setClassTitle("Data Structure"); // sets subject
        schedule3.setClassPlace("IT-601"); // sets place
        schedule3.setProfessorName("Won Kim"); // sets professor
        schedule3.setStartTime(new Time(14,0)); // sets the beginning of class time (hour,minute)
        schedule3.setEndTime(new Time(15, 45));
        schedule3.setDay(0);
        schedules.add(schedule3);

        Schedule schedule4 = new Schedule();
        schedule4.setClassTitle("Data Structure"); // sets subject
        schedule4.setClassPlace("IT-601"); // sets place
        schedule4.setProfessorName("Won Kim"); // sets professor
        schedule4.setStartTime(new Time(16,0)); // sets the beginning of class time (hour,minute)
        schedule4.setEndTime(new Time(18, 50));
        schedule4.setDay(0);
        schedules.add(schedule4);

        timetable.add(schedules);


        ArrayList<Schedule> schedules1 = new ArrayList<Schedule>();

        Schedule schedule1 = new Schedule();
        schedule1.setClassTitle("Computer"); // sets subject
        schedule1.setClassPlace("IT-60221"); // sets place
        schedule1.setProfessorName("Won Ki22m"); // sets professor
        schedule1.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule1.setEndTime(new Time(13,45)); // sets the end of class time (hour,minute)
        schedule1.setDay(1);
        schedules1.add(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setClassTitle("Computer"); // sets subject
        schedule2.setClassPlace("IT-60221"); // sets place
        schedule2.setProfessorName("Won Ki22m"); // sets professor
        schedule2.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule2.setEndTime(new Time(13,45)); // sets the end of class time (hour,minute)
        schedule2.setDay(2);
        schedules1.add(schedule2);

        timetable.add(schedules1);


        ArrayList<Schedule> schedules2 = new ArrayList<Schedule>();

        Schedule schedule5 = new Schedule();
        schedule5.setClassTitle("Computer"); // sets subject
        schedule5.setClassPlace("IT-60221"); // sets place
        schedule5.setProfessorName("Won Ki22m"); // sets professor
        schedule5.setStartTime(new Time(14,30)); // sets the beginning of class time (hour,minute)
        schedule5.setEndTime(new Time(16,50)); // sets the end of class time (hour,minute)
        schedule5.setDay(2);
        schedules2.add(schedule5);

        Schedule schedule6 = new Schedule();
        schedule6.setClassTitle("Computer"); // sets subject
        schedule6.setClassPlace("IT-60221"); // sets place
        schedule6.setProfessorName("Won Ki22m"); // sets professor
        schedule6.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule6.setEndTime(new Time(13,45)); // sets the end of class time (hour,minute)
        schedule6.setDay(3);
        schedules2.add(schedule6);

        timetable.add(schedules2);

    }
}

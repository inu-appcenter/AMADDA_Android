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
        timetable.setHeaderHighlight(1);

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

        Schedule schedule1 = new Schedule();
        schedule1.setClassTitle("Computer"); // sets subject
        schedule1.setClassPlace("IT-60221"); // sets place
        schedule1.setProfessorName("Won Ki22m"); // sets professor
        schedule1.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule1.setEndTime(new Time(13,45)); // sets the end of class time (hour,minute)
        schedule1.setDay(1);
        schedules.add(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setClassTitle("Computer"); // sets subject
        schedule2.setClassPlace("IT-60221"); // sets place
        schedule2.setProfessorName("Won Ki22m"); // sets professor
        schedule2.setStartTime(new Time(12,0)); // sets the beginning of class time (hour,minute)
        schedule2.setEndTime(new Time(13,45)); // sets the end of class time (hour,minute)
        schedule2.setDay(2);
        schedules.add(schedule2);


        timetable.add(schedules);

    }
}

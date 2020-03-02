package com.inu.amadda.timetable;

import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class Sticker implements Serializable {
    private ArrayList<RelativeLayout> view;
    private ArrayList<Schedule> schedules;

    public Sticker() {
        this.view = new ArrayList<RelativeLayout>();
        this.schedules = new ArrayList<Schedule>();
    }

    public void addTextView(RelativeLayout v){
        view.add(v);
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public ArrayList<RelativeLayout> getView() {
        return view;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}

package com.inu.amadda.model;

import com.inu.amadda.timetable.Schedule;

public class ScheduleWithKey {
    public int key;
    public Schedule schedule;

    public ScheduleWithKey(int key, Schedule schedule) {
        this.key = key;
        this.schedule = schedule;
    }
}

package com.inu.amadda.model;

public class DaySchedule implements Comparable<DaySchedule> {
    private boolean isSchedule;
    private int number, share;
    private String schedule_name, start, end, location, memo, color;

    public boolean isSchedule() {
        return isSchedule;
    }

    public void setSchedule(boolean schedule) {
        isSchedule = schedule;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public DaySchedule(boolean isSchedule, int number, int share, String schedule_name, String start, String end, String location, String memo, String color) {
        this.isSchedule = isSchedule;
        this.number = number;
        this.share = share;
        this.schedule_name = schedule_name;
        this.start = start;
        this.end = end;
        this.location = location;
        this.memo = memo;
        this.color = color;
    }

    @Override
    public int compareTo(DaySchedule daySchedule) {
        return this.start.compareTo(daySchedule.start);
    }
}

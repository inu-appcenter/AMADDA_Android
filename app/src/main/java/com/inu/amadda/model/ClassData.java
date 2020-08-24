package com.inu.amadda.model;

import java.io.Serializable;

public class ClassData implements Serializable {
    private String lecture, professor, day, start, end, room;

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public ClassData(String lecture, String professor, String day, String start, String end, String room) {
        this.lecture = lecture;
        this.professor = professor;
        this.day = day;
        this.start = start;
        this.end = end;
        this.room = room;
    }
}

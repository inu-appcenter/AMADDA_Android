package com.inu.amadda.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayDate {
    private static Date today = new Date();

    public static int getDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.KOREAN);
        String day = sdf.format(today);
        switch (day) {
            case "월":
                return 1;
            case "화":
                return 2;
            case "수":
                return 3;
            case "목":
                return 4;
            case "금":
                return 5;
            default:
                return -1;
        }
    }

    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 E요일", Locale.KOREAN);
        return sdf.format(today);
    }
}

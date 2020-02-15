package com.inu.amadda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodayDate {
    private static long now = System.currentTimeMillis();
    private static Date date = new Date(now);

    public static int getDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String day = sdf.format(date);
        switch (day) {
            case "월요일":
                return 1;
            case "화요일":
                return 2;
            case "수요일":
                return 3;
            case "목요일":
                return 4;
            case "금요일":
                return 5;
            default:
                return -1;
        }
    }
}

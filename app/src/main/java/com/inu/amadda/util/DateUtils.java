package com.inu.amadda.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static Date now = new Date();
    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public static int getDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.KOREAN);
        String day = sdf.format(now);
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
        return sdf.format(now);
    }
}

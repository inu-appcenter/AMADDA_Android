package com.inu.amadda.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtils {
    public static LocalDate now = LocalDate.now();
    public static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static String dateFormat = "yyyy-MM-dd";

    public static int getDayOfWeek() {
        String day = now.format(DateTimeFormatter.ofPattern("E", Locale.KOREAN));
        switch (day) {
            case "일":
                return 0;
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
            case "토":
                return 6;
            default:
                return -1;
        }
    }

    public static String getTitleString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN));
    }
}

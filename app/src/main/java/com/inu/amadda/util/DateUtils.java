package com.inu.amadda.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtils {
    public static final int MON = 0;
    public static final int TUE = 1;
    public static final int WED = 2;
    public static final int THU = 3;
    public static final int FRI = 4;
    public static final int SAT = 5;
    public static final int SUN = 6;

    public static LocalDate now = LocalDate.now();
    public static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static String dateFormat = "yyyy-MM-dd";

    public static int getDayOfWeek() {
        String day = now.format(DateTimeFormatter.ofPattern("E", Locale.KOREAN));
        switch (day) {
            case "월":
                return MON;
            case "화":
                return TUE;
            case "수":
                return WED;
            case "목":
                return THU;
            case "금":
                return FRI;
            case "토":
                return SAT;
            case "일":
                return SUN;
            default:
                return -1;
        }
    }

    public static String getTitleString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN));
    }
}

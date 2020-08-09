package com.inu.amadda.util;

public class Utils {

    public static String toStringColor(int intColor) {
        return String.format("#%06X", 0xFFFFFF & intColor);
    }
}

package com.inu.amadda.util;

import android.content.Context;

import com.inu.amadda.R;

import java.util.Random;

public class Utils {

    public static String toStringColor(int intColor) {
        return String.format("#%06X", 0xFFFFFF & intColor);
    }

    public static String getRandomColor(Context context){
        int[] colorArray = context.getResources().getIntArray(R.array.color_picker_array);
        int randomColor = colorArray[new Random().nextInt(colorArray.length)];
        return toStringColor(randomColor);
    }
}

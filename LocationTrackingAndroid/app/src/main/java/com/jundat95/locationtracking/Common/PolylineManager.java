package com.jundat95.locationtracking.Common;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by tinhngo on 3/3/17.
 */

public class PolylineManager {

    private static Random random = new Random();

    private static List<Integer> polyline = Arrays.asList(
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.DKGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.RED,
            Color.YELLOW
    );

    public static Integer getColors(){
        int rd = random.nextInt(polyline.size());
        return polyline.get(rd);
    }

    public static Integer getColors(int index){
        return polyline.get(index);
    }
}

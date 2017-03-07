package com.jundat95.locationtracking.Common;


import android.graphics.Color;

import com.jundat95.locationtracking.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by tinhngo on 3/2/17.
 */

public class MarkerManager {

    private static Random random = new Random();

    private static List<Integer> markers = Arrays.asList(
            R.drawable.mk1,
            R.drawable.mk9,
            R.drawable.mk12,
            R.drawable.mk4,
            R.drawable.mk5,
            R.drawable.mk6,
            R.drawable.mk7,
            R.drawable.mk8,
            R.drawable.mk2,
            R.drawable.mk10,
            R.drawable.mk11,
            R.drawable.mk3
    );

    public static Integer getImages(){
        int rd = random.nextInt(markers.size());
        return markers.get(rd);
    }
    public static Integer getImages(int index){
        return markers.get(index);
    }

}

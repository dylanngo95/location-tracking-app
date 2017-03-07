package com.jundat95.locationtracking.Common;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tinhngo on 2/19/17.
 */

public class CustomColor {

    public static String getRandomColor() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#ED7D31");
        colors.add("#00B0F0");
        colors.add("#FF0000");
        colors.add("#D0CECE");
        colors.add("#00B050");
        colors.add("#9999FF");
        colors.add("#FF5FC6");
        colors.add("#FFC000");
        colors.add("#7F7F7F");
        colors.add("#4800FF");

        return colors.get(new Random().nextInt(colors.size()));
    }
}

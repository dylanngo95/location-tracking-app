package com.jundat95.locationtracking.Common;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by tinhngo on 3/5/17.
 */

public class DateTimeManager {
    public static  String getTimeFromTimeStamp(String timestamp) {
        // Convert time stamp string to datetime
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long currentTime = Long.parseLong(timestamp);
            Timestamp stamp = new Timestamp(currentTime);
            Date date = new Date(stamp.getTime());
            return sdf.format(date);
        }catch (Exception ex){
            return "";
        }

    }
}

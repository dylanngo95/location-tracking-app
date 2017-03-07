package com.jundat95.locationtracking.API;

/**
 * Created by tinhngo on 2/15/17.
 */

public class APIConfig {
    public static final String BASE_URL = "http://192.168.253.119:8080/";
    public static final String DEMO_TRACKING = "http://lotra.mybluemix.net/api/v1/";

    public static String getBaseUrl(){
        return DEMO_TRACKING;
    }
}

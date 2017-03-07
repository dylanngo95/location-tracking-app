package com.jundat95.locationtracking.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jundat95.locationtracking.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tinhngo on 2/15/17.
 */

public interface APIGPSGroupInterface {

    @GET("nodes")
    Call<ResponseModel> getAllPositions();

    @GET("nodes/{nodeId}")
    Call<ResponseModel> getPosition(@Path("nodeId") String nodeId);

    class Factory{
        public static APIGPSGroupInterface service;
        public static APIGPSGroupInterface getInstance(){
            if(service == null){
                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIConfig.getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                service = retrofit.create(APIGPSGroupInterface.class);
                return service;

            } else {
                return service;
            }
        }
    }

}


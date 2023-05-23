package com.example.myapplication.Retrofit;

import com.example.myapplication.Trip1.Trip1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TripAPI {

    @GET("/api/v1/trip")
    Call<List<Trip1>> getTrips() ;

    @POST("/api/v1/trip")
    Call<Trip1> save(@Body Trip1 trip1) ;

}

package com.example.myapplication.Retrofit;

import com.example.myapplication.Payment.Payment;
import com.example.myapplication.User.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PaymentAPI {

    @POST("/api/v1/payment")
    Call<Payment> save(@Body Payment payment) ;

    @GET("/api/v1/payment")
    Call<List<Payment>> getPayments() ;

}

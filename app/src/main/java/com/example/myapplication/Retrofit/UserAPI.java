package com.example.myapplication.Retrofit;

import com.example.myapplication.Trip1.Trip1;
import com.example.myapplication.User.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("/api/v1/user")
    Call<List<User>> getUsers() ;

    @POST("/api/v1/user")
    Call<User> save(@Body User user) ;

    @PUT("/api/v1/user/{tripID}/users/{userID}")
    Call<User> addUserToTrip (
            @Path("tripID") Long tripId ,
            @Path("userID") Long userId

            ) ;

    @PUT("/api/v1/user/{tripID}/users-remove/{userID}")
    Call<User> userRemoveTrip (
            @Path("tripID") Long tripId ,
            @Path("userID") Long userId

    ) ;


//    @PutMapping(path ="/{tripId}/users-remove/{userId}")
//    public ResponseEntity<User> userRemoveTrip(@PathVariable Long tripId,@PathVariable Long userId) {
//        tripService.removeUserFromTrip(tripId,userId);
//        userService.removeTripFromUser(tripId,userId);
//        return new ResponseEntity<User>(HttpStatus.OK);
//    }




}

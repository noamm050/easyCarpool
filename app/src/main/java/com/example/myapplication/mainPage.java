package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mainPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    /**
     * Move to Start a trip activity
     * @param v connect to xml
     */
    public void startPress(View v) {
        Intent intent = new Intent(this,TripExecuter.class) ;
        startActivity(intent) ;
    }

    /**
     * Move to user's profile activity
     * @param v for xml
     */
    public void profilePress(View v) {
        Intent intent = new Intent(this,profile.class) ;
        startActivity(intent) ;
    }

    /**
     * Log Out - Go back To Log In Page
     * @param v for xml
     */
    public void logOutPress (View v) {
        Intent intent = new Intent(this,MainActivity.class) ;
        startActivity(intent) ;
    }

    public void activeTripsPress (View v) {
        Intent intent = new Intent(this,acountTrips.class) ;
        startActivity(intent) ;
    }

}
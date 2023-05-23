package com.example.myapplication;


import static com.example.myapplication.TripDetails.isPassenger;
import static com.example.myapplication.TripDetails.lengthOfTrip;
import static com.example.myapplication.TripExecuter.originString;
import static com.example.myapplication.TripExecuter2.destinationString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.TripAPI;
import com.example.myapplication.Trip1.Trip1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initialize() ;
    }

    /**
    For Passengers: Extracts the driver's name, and the cost of the trip
     */
    public void initialize () {

        EditText o = (EditText) findViewById(R.id.origin);
        o.setText(originString, TextView.BufferType.EDITABLE);

        EditText d = (EditText) findViewById(R.id.destination);
        d.setText(destinationString,TextView.BufferType.EDITABLE);

            double costOfTrip = round(lengthOfTrip/2000 , 2) ;
            EditText c = (EditText) findViewById(R.id.cost);
            c.setText(String.valueOf(costOfTrip), TextView.BufferType.EDITABLE);
    }



    /**
     * Round the double value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /*
    Go back home
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }
}


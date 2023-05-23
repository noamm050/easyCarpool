package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripExecuter2 extends AppCompatActivity {

    private EditText mSearchText2 ;
    public static double destinationLat ;
    public static double destinationLon ;
    public static String destinationString ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_executer2);
        mSearchText2 = (EditText) findViewById(R.id.DestinationText);
    }

    /**
     * Geolocate the origin Address - latitude and longlitude
     * Through Google's API
     */
    private void geoLocate () {
        String searchString = mSearchText2.getText().toString();
        Geocoder geocoder = new Geocoder(TripExecuter2.this) ;
        List<Address> list = new ArrayList<>() ;
        try {
            list = geocoder.getFromLocationName(searchString,1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (list.size()>0) {
            Address destination = list.get(0) ;
            Log.d(TAG,"location" +destination.toString()) ;
            destinationLat = destination.getLatitude() ;
            destinationLon = destination.getLongitude() ;
            destinationString =  destination.getAddressLine(0);
            System.out.println(destinationString);
            Intent intent = new Intent(this,DatePicker.class) ;
            startActivity(intent) ;
        }
    }

    /**
     * Go back Home
     * @param v for xml
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }

    /**
     * When user is ready it will GeoLocate
     * @param v for xml
     */
    public void btnClick (View v) {
        geoLocate();
    }
}
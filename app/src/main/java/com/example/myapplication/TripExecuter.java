package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripExecuter extends AppCompatActivity {

    private EditText mSearchText ;
    public static double originLat ;
    public static double originLon ;
    public static String originString ;
    boolean flag = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_executer);
        mSearchText = (EditText) findViewById(R.id.OriginText);
    }

    /**
     * Geolocate the origin Address - latitude and longlitude
     * Through Google's API
     */
    private void geoLocate () {

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(TripExecuter.this) ;
        List<Address> list = new ArrayList<>() ;
        try {
            list = geocoder.getFromLocationName(searchString,1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (list.size()>0) {
            Address origin = list.get(0) ;
            Log.d(TAG,"location" +origin.toString()) ;
            originLat = origin.getLatitude() ;
            originLon = origin.getLongitude() ;
            originString = origin.getAddressLine(0);
            System.out.println(originString);
            Intent intent = new Intent(this,TripExecuter2.class) ;
            startActivity(intent) ;

        }
    }

    /**
     * Go back Home
     * @param v
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }

    /**
     * When user is ready it will locate the desired Address
     * @param v
     */
    public void btnClick (View v) {
        geoLocate() ;
    }
}
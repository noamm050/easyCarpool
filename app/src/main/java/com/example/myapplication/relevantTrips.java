package com.example.myapplication;

import static com.example.myapplication.DatePicker.DateString;
import static com.example.myapplication.TripDetails.timeOfTrip;
import static com.example.myapplication.TripExecuter.originLat;
import static com.example.myapplication.TripExecuter.originLon;
import static com.example.myapplication.TripExecuter2.destinationLat;
import static com.example.myapplication.TripExecuter2.destinationLon;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.TripAPI;
import com.example.myapplication.Trip1.Trip1;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class relevantTrips extends AppCompatActivity {


    // To show all relevant trips
    List<String> list = new ArrayList<>();
    // To store the driver's id's (for identification)
    Long [] driverIds = new Long [100] ;
    int positionInArray ;
    public static Long driverId ;
    private boolean RelevantRadius = false ;
    private boolean RelevantTime = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevant_trips);
        init();
    }

    /**
     * initialize : Check for relevant trips in the Database of trips that are :
     * drivers that suggest a trip,
     * in the desired date,
     * still have room in the car,
     * in the desired time +-2 hours difference
     * With Radius of 5KM from passenger's location
     */
    public void init() {

        RetrofitService retrofitService = new RetrofitService();
        TripAPI tripAPI = retrofitService.getRetrofit().create(TripAPI.class);

        //Get all trips and search for a match
        Call<List<Trip1>> call = tripAPI.getTrips();
        call.enqueue(new Callback<List<Trip1>>() {
            @Override
            public void onResponse(Call<List<Trip1>> call, Response<List<Trip1>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(relevantTrips.this, "EMPTY DATABASE...", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if RelevantRadius() ;
                // if Time() ;
                // Parse Local Time +-Hour

                List<Trip1> trips = response.body();
                assert trips != null ;
                for (Trip1 trip1 : trips) {
                    if (trip1.getDriverId() != null
                             && trip1.getStartDate().equals(DateString)
                                && trip1.getPassengerCounter() < trip1.getPassengerCapacity()
                    ) {

                        String tripAddressOrigin = trip1.getOrigin() ;
                        String tripAddressDestination = trip1.getDestination() ;
                        String tripTime = trip1.getStartHour() ;
                        Time(tripTime) ;

                        if (!RelevantTime) {
                            Toast.makeText(relevantTrips.this,
                                    "      No Trip Available At this time... \nYou can press back and pick another time", Toast.LENGTH_LONG).show();
                        }
                        if (RelevantTime) {

                            Radius(tripAddressOrigin,tripAddressDestination) ;

                            if (RelevantRadius) {

                                String hour = trip1.getStartHour();
                                driverIds[positionInArray] = trip1.getDriverId();
                                positionInArray++;
                                list.add("PICK: " + hour);

                            }
                        }
                    }
                }
                createList();
            }

            @Override
            public void onFailure(Call<List<Trip1>> call, Throwable t) {
               // Toast.makeText(relevantTrips.this, "ERROR IN CALLING!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "ERROR!", t);
            }
        });
    }

    /**
     * A method That searches for relevant trips in a 5KM Radius from the user's location
     * @param tripAddressOrigin The origin address to look for radius of
     * @param tripAddressDestination The destination address to look for radius of
     *
     */
    private void Radius(String tripAddressOrigin, String tripAddressDestination) {

        double tripAddressLat = 0, tripAddressLon=0, tripAddressLat2 = 0, tripAddressLon2 =0 ;

        Geocoder geocoder = new Geocoder(relevantTrips.this) ;
        List<Address> list = new ArrayList<>() ;
        List<Address> list2 = new ArrayList<>() ;

        /**
         * CHECK ORIGIN
         */
        try {
            list = geocoder.getFromLocationName(tripAddressOrigin,1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (list.size()>0) {
            Address trip = list.get(0);
            tripAddressLat = trip.getLatitude();
            tripAddressLon = trip.getLongitude();
        }
        float[] results = new float[1];
        Location.distanceBetween(originLat,originLon,tripAddressLat,tripAddressLon,results);

        /**
         * CHECK DESTINATION
         */
        try {
            list2 = geocoder.getFromLocationName(tripAddressDestination,1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (list2.size()>0) {
            Address trip2 = list2.get(0);
            tripAddressLat2 = trip2.getLatitude();
            tripAddressLon2 = trip2.getLongitude();
        }
        float[] results2 = new float[1];
        Location.distanceBetween(destinationLat,destinationLon,tripAddressLat2,tripAddressLon2,results2);

        // RESULTS
        float distanceInMetersOrigin = results[0];
        float distanceInMetersDestination = results2[0];

        if (distanceInMetersOrigin<5000 && distanceInMetersDestination<5000) // 5 KM Radius
            RelevantRadius = true ;

    }

    /**
     * A method that searches for relevant time of trips +-2 Hours
     * @param Time time of trip
     */
    private void Time (String Time) {

        LocalTime tripTime = LocalTime.parse(Time) ;
        LocalTime userTime = LocalTime.parse(timeOfTrip) ;

        int hourOfTrip = tripTime.getHour() ;
        int hourOfUser = userTime.getHour() ;

        if (hourOfTrip+2 > hourOfUser && hourOfTrip-2 < hourOfUser)
            RelevantTime = true ;

    }


    /**
     * A function that shows all relevant trips in a list on the user's screen activity
     * The user's choice will get the driver's id to show his details
     */
    public void createList() {

    ListView listView = findViewById(R.id.simpleListView);

    // SHOW ALL RELEVANT TRIPS IN LISTVIEW
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, list) ;
        listView.setAdapter(arrayAdapter);
        Intent intent = new Intent(relevantTrips.this,driverDetails.class) ;

        //Move to MAP
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int p = 0; p < list.size(); p++) {
                    if (p == i) {
                        driverId = driverIds[p] ;

                        startActivity(intent) ;
                    }
                }
            }
        });

    }


    /**
     * Go back home
     * @param v for xml
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }

    /**
     * The opportunity of no relevant trips could be switched in a different time choice
     * So user can go back here
     * @param v for xml
     */
    public void backPress(View v) {
        Intent intent = new Intent(relevantTrips.this, TripDetails.class) ;
        startActivity(intent);
    }

}
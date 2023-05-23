package com.example.myapplication;

import static com.example.myapplication.DatePicker.DateString;
import static com.example.myapplication.MainActivity.userId;
import static com.example.myapplication.TripExecuter.originString;
import static com.example.myapplication.TripExecuter.originLat;
import static com.example.myapplication.TripExecuter.originLon;
import static com.example.myapplication.TripExecuter2.destinationString;
import static com.example.myapplication.TripExecuter2.destinationLat;
import static com.example.myapplication.TripExecuter2.destinationLon;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.TripAPI;
import com.example.myapplication.Trip1.Trip1;

import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetails extends AppCompatActivity {

    //Number of Passengers in the trip / Number of available slots in the trips
    public static int numOfPass ;
    public static int myHour ;
    // Length of trip in KM
    public static double lengthOfTrip ;
    // The time of the trip
    public static String timeOfTrip ;
    // Is the user a Passenger ?
    public static boolean isPassenger = false ;
    Button timeButton ;
    int hour, minute ;
    private boolean timeChoosed = false ;
    EditText numberOfPassengers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        timeButton = findViewById(R.id.timeButton) ;

    }


    /**
     * If the user is a driver it saves his details and the trip details in the 'Trips' database
     * Now when a passenger will look for a close trip it will be suggested
     * @param v for xml
     */
    public void driverPress (View v) {

        RetrofitService retrofitService = new RetrofitService();
        TripAPI tripAPI = retrofitService.getRetrofit().create(TripAPI.class);

        numberOfPassengers = (EditText) findViewById(R.id.numberOfPass) ;
        numOfPass = Integer.parseInt(numberOfPassengers.getText().toString()) ;

        if(!timeChoosed) {
            Toast.makeText(this, "CHOOSE TIME!", Toast.LENGTH_SHORT).show();
        }
        else {


            float[] results = new float[1];
            Location.distanceBetween(originLat, originLon, destinationLat, destinationLon, results);
            float res = results[0] / 1000 ;
            lengthOfTrip = (double) res ;


            Trip1 trip1 = new Trip1();
            trip1.setOrigin(originString);
            trip1.setDestination(destinationString);
            trip1.setStartHour(timeOfTrip);
            trip1.setDistance(lengthOfTrip);
            trip1.setDriverId(userId);
            trip1.setPassengerCapacity(numOfPass);
            trip1.setPassengerCounter(0);
            trip1.setStartDate(DateString);

            Intent intent = new Intent(TripDetails.this, mapActivity.class);

            tripAPI.save(trip1)
                    .enqueue(new Callback<Trip1>() {
                        @Override
                        public void onResponse(Call<Trip1> call, Response<Trip1> response) {
                           // Toast.makeText(TripDetails.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                            // tripId = trip1.getTripId() ;
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Trip1> call, Throwable t) {
                          //  Toast.makeText(TripDetails.this, "ERROR!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(TripDetails.class.getName()).log(Level.SEVERE, "ERROR!", t);
                        }
                    });
        }
    }

    /**
     * A passenger will move to relevant trips activity
     * @param v for xml
     */
    public void passengerPress (View v) {

        numberOfPassengers = (EditText) findViewById(R.id.numberOfPass) ;
        numOfPass = Integer.parseInt(numberOfPassengers.getText().toString()) ;

        if(!timeChoosed) {
            Toast.makeText(this, "CHOOSE TIME!", Toast.LENGTH_SHORT).show();
        }
        else {
            float[] results = new float[1];
            Location.distanceBetween(originLat, originLon, destinationLat, destinationLon, results);
            lengthOfTrip = results[0];

            Intent intent = new Intent(this, relevantTrips.class);
            startActivity(intent);
        }


    }

    /**
     * Go back home
     * @param v
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }

    /**
     * To choose time ... A time picker
     * @param view
     */
    public void popTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                timeOfTrip = String.format(Locale.getDefault(), "%02d:%02d",hour, minute) ;
                System.out.println(timeOfTrip);
                myHour = selectedHour ;
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
        timeChoosed = true ;

    }
}
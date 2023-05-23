package com.example.myapplication;

import static com.example.myapplication.MainActivity.userEmail;
import static com.example.myapplication.MainActivity.userFirstName;
import static com.example.myapplication.MainActivity.userId;
import static com.example.myapplication.MainActivity.userLastName;
import static com.example.myapplication.MainActivity.userPassword;
import static com.example.myapplication.TripDetails.isPassenger;
import static com.example.myapplication.TripDetails.lengthOfTrip;
import static com.example.myapplication.relevantTrips.driverId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Payment.Payment;
import com.example.myapplication.Retrofit.PaymentAPI;
import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.TripAPI;
import com.example.myapplication.Retrofit.UserAPI;
import com.example.myapplication.Trip1.Trip1;
import com.example.myapplication.User.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class driverDetails extends AppCompatActivity {

    public Long tripId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        init(); // Update Driver's details in the specific drive
        init2(); // Get the trip ID of that trip

    }

    /**
     * To find the specific driver's details for updates, payments, trips and costs
     */
    public void init() {

        // RUN ON USERS DATABASE AND FIND THE SPECIFIC DRIVER'S DETAILS FOR THE RIDE

        EditText fN = (EditText) findViewById(R.id.DriverFirstName);
        EditText lN = (EditText) findViewById(R.id.DriverLastName);

        double costOfTrip = round(lengthOfTrip/2000 , 2) ;
        EditText cOt = (EditText) findViewById(R.id.costOfTripText);
        cOt.setText(String.valueOf(costOfTrip), TextView.BufferType.EDITABLE);


        RetrofitService retrofitService = new RetrofitService() ;
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class) ;

        Call<List<User>> call = userAPI.getUsers() ;
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(driverDetails.this, "EMPTY DATABASE...", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<User> users = response.body() ;
                assert users != null;
                for (User user : users) {
                    if ( user.getUserId() == driverId ) {
                        fN.setText(user.getFirstName(), TextView.BufferType.EDITABLE);
                        lN.setText(user.getLastName(), TextView.BufferType.EDITABLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
              //  Toast.makeText(driverDetails.this, "ERROR!",Toast.LENGTH_SHORT).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
            }
        });


    }

    /**
     * To find the specific trip for updates
     */
    public void init2 () {

        // RUN ON TRIPS AND FIND THE SPECIFIC TRIP ID OF THE RIDE

        EditText dO = (EditText) findViewById(R.id.DriverOrigin);
        EditText dD = (EditText) findViewById(R.id.DriverDestination);


        RetrofitService retrofitService = new RetrofitService() ;
        TripAPI tripAPI = retrofitService.getRetrofit().create(TripAPI.class) ;
        Call<List<Trip1>> call = tripAPI.getTrips() ;
        call.enqueue(new Callback<List<Trip1>>() {
            @Override
            public void onResponse(Call<List<Trip1>> call, Response<List<Trip1>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(driverDetails.this, "EMPTY DATABASE...", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Trip1> trips = response.body() ;
                assert trips != null;
                for (Trip1 trip1 : trips) {
                    if ( trip1.getDriverId() == driverId ) {
                        tripId = trip1.getTripId()  ;
                        dO.setText(trip1.getOrigin(), TextView.BufferType.EDITABLE);
                        dD.setText(trip1.getDestination(), TextView.BufferType.EDITABLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Trip1>> call, Throwable t) {
               // Toast.makeText(driverDetails.this, "ERROR!",Toast.LENGTH_SHORT).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
            }
        });
    } //end of function init2()


    /**
     *     Update the 'trips' Database in the user's trips
     */
    public void init3() {

        // ADD A TRIP TO A SPECIFIC USER

        RetrofitService retrofitService = new RetrofitService() ;
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class) ;


        userAPI.addUserToTrip(tripId,userId /*,user*/)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(driverDetails.this, "SUCCESS!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                      //  Toast.makeText(driverDetails.this, "ERROR!",Toast.LENGTH_SHORT).show();
                        Logger.getLogger(TripDetails.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
                    }
                });
    }


    /**
     * Update the 'payment' database for the trip, the users passenger and driver and the amount
     */
    public void init4 () {

        // ADD A NEW PAYMENT

        Payment payment = new Payment() ;
        payment.setAmount(round(lengthOfTrip/2000 , 2));
        payment.setFromId(userId);
        payment.setToId(driverId);
        payment.setTripId(tripId);

        RetrofitService retrofitService = new RetrofitService() ;
        PaymentAPI paymentAPI = retrofitService.getRetrofit().create(PaymentAPI.class) ;
        paymentAPI.save(payment)
                .enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {
                        Toast.makeText(driverDetails.this, "SUCCESS!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                       // Toast.makeText(driverDetails.this, "ERROR!",Toast.LENGTH_SHORT).show();
                        Logger.getLogger(signUp.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
                    }
                });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }

    public void startPress(View v) {
        isPassenger = true ;
        init3() ; //Update the trip in the user's trips database
        init4() ; // Update the payment database
        Intent intent = new Intent(driverDetails.this,mapActivity.class) ;
        startActivity(intent);
    }

    public void addMePress(View v) {
        isPassenger = true ;
        init3() ; //Update the trip in the user's trips database
        //init4() ; // Update the payment database
        Intent intent = new Intent(driverDetails.this,acountTrips.class) ;
        startActivity(intent);
    }

    public void backPress(View v) {
        Intent intent = new Intent(driverDetails.this, relevantTrips.class) ;
        startActivity(intent);
    }
}
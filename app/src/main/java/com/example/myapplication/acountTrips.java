package com.example.myapplication;

import static com.example.myapplication.DatePicker.myDayOfMonth;
import static com.example.myapplication.DatePicker.myMonth;
import static com.example.myapplication.DatePicker.myYear;
import static com.example.myapplication.MainActivity.userId;
import static com.example.myapplication.TripDetails.myHour;
import static com.example.myapplication.signUp.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.UserAPI;
import com.example.myapplication.Trip1.Trip1;
import com.example.myapplication.User.User;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class acountTrips extends AppCompatActivity {


    List<String> list = new ArrayList<>();
    Long [] driverIds = new Long [100] ;
    int positionInArray ;
    long driverId ;
    Set<Trip1> trips = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_trips);
        init();
    }

    public void init() {


        RetrofitService retrofitService = new RetrofitService();
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

        Call<List<User>> call = userAPI.getUsers() ;
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

//                if (!response.isSuccessful()) {
//                    //Toast.makeText(acountTrips.this, "EBCHBDHSBCHJSBCHJS...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                List<User> users = response.body();
                assert users != null;
                for (User user : users) {
                    if (user.getUserId() == (userId)) {
                        trips = user.getTrips();
                        for (Trip1 trip1 : trips) {
                            if (myDayOfMonth >= Calendar.DAY_OF_MONTH
                                && myMonth >= Calendar.MONTH
                                    && myYear >= Calendar.YEAR
                                       /* && myHour >= LocalTime.now().getHour()*/) {
                            driverIds[positionInArray] = trip1.getDriverId();
                            positionInArray++;
                            list.add("Trip time: " + trip1.getStartHour() + " , Trip date: "
                                    + trip1.getStartDate());
                             }
                        }
                    }
                }
                createList();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "ERROR!", t);
            }
        });
    }
    public void createList() {

        ListView listView = findViewById(R.id.simpleListView);

        // SHOW ALL RELEVANT TRIPS IN LISTVIEW
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, list) ;
        listView.setAdapter(arrayAdapter);
        Intent intent = new Intent(acountTrips.this,startTripFromList.class) ;

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

    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }
}




//                            String myDate = trip1.getStartDate() ;
//                            int day=0, month=0, year=0, hour ;
//                            int flag = 0, j=0 ;
//                            for (int i = 0; i < myDate.length() ; i++) {
//                                if (myDate.charAt(i)=='/' && flag == 0) {
//                                        day = Integer.parseInt(myDate.substring(0,i)) ;
//                                        flag++ ;
//                                        j=i+1 ;
//                                }
//                                if (myDate.charAt(i)=='/' && flag == 1) {
//                                    month = Integer.parseInt(myDate.substring(j,i)) ;
//                                    flag++ ;
//                                    j=i+1 ;
//                                }
//                                if (flag==2) {
//                                    year = Integer.parseInt(myDate.substring(j, myDate.length()));
//                                    break;
//                                }
//                            }
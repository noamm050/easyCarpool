package com.example.myapplication;

import static com.example.myapplication.MainActivity.userEmail;
import static com.example.myapplication.MainActivity.userFirstName;
import static com.example.myapplication.MainActivity.userId;
import static com.example.myapplication.MainActivity.userLastName;

import static java.lang.Long.sum;

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
import com.example.myapplication.Retrofit.UserAPI;
import com.example.myapplication.User.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {

    UserAPI userAPI ;
    double allPaid = 0, allReceived = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    /**
     * Initialize Method
     * All Details of the user are shown to the activity's screen
     */
    public void init() {


        EditText FirstName = (EditText) findViewById(R.id.textViewFirstName);
        FirstName.setText(userFirstName, TextView.BufferType.EDITABLE);

        EditText LastName = (EditText) findViewById(R.id.textViewLastName);
        LastName.setText(userLastName, TextView.BufferType.EDITABLE);

        EditText Email = (EditText) findViewById(R.id.textViewEmail);
        Email.setText(userEmail, TextView.BufferType.EDITABLE);

        EditText paid = (EditText) findViewById(R.id.Paid);
        EditText received = (EditText) findViewById(R.id.Received);

        /**
         * Calculate All trips the user's have ever done as a driver and a passenger
         * and sum their values
         */
        RetrofitService retrofitService = new RetrofitService();
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);
        Call<List<User>> call = userAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                assert users != null;
                for (User user : users) {
                    if (user.getUserId() == userId) {
                        String userPaid = String.valueOf(user.getPaid());
                        String userReceived = String.valueOf(user.getReceived());
                        paid.setText(userPaid, TextView.BufferType.EDITABLE);
                        received.setText(userReceived, TextView.BufferType.EDITABLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    /**
     * Go back to home page
     * @param v for xml file show
     */
    public void homePress (View v) {
        Intent intent = new Intent(this,mainPage.class) ;
        startActivity(intent) ;
    }
}
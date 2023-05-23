package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.UserAPI;
import com.example.myapplication.User.User;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public static String userEmail ;
    public static String userFirstName ;
    public static String userLastName ;
    public static Long userId ;
    public static String userPassword ;

    private boolean flag = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Connect To all users DataBase and check if the user exists with email and password
     * Id doesn't exist you can sign up
     * @param v connect to xml
     */
    public void loginPress (View v) {


        RetrofitService retrofitService = new RetrofitService() ;
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class) ;

        /**
         * TO MOVE PAGE WHEN SUCCESSFUL LOGIN
         */
        EditText e = findViewById(R.id.nameText);
        String email = e.getText().toString() ;
        EditText p = findViewById(R.id.passwordText);
        String password = p.getText().toString() ;

        Intent intent = new Intent(this, mainPage.class);


        /**
         * TO CONNECT TO DATABASE od users and pull user's details to the system
         */
        Call<List<User>> call = userAPI.getUsers() ;
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "EMPTY DATABASE...", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<User> users = response.body() ;
                assert users != null;
                for (User user : users) {
                    //System.out.println(user.getEmail());
                    /**
                     * To save User's Details in the system
                     */
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        userEmail = user.getEmail() ;
                        userFirstName = user.getFirstName() ;
                        userLastName = user.getLastName() ;
                        userId = user.getUserId() ;
                        userPassword = user.getPassword();
                        startActivity(intent);
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR!",Toast.LENGTH_SHORT).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
            }
        });
    }

    /**
     * Move to sign Up Page
     */
    public void signUpPress (View v) {
        Intent intent = new Intent(this,signUp.class) ;
        startActivity(intent) ;
    }

}
package com.example.myapplication;

import static com.example.myapplication.relevantTrips.driverId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Retrofit.RetrofitService;
import com.example.myapplication.Retrofit.UserAPI;
import com.example.myapplication.User.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signUp extends AppCompatActivity {

    boolean flag = false ;
    public static String firstName ;
    public static String lastName ;
    public static String email ;
    public static String password ;

    boolean goodPassword = false ;
    boolean goodEmail = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * Submit a user, save user's details in the system and in the Database od Users
     * @param v for xml
     */
    public void submitPress (View v) {


        RetrofitService retrofitService = new RetrofitService();
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);


        EditText f = findViewById(R.id.firstName);
        firstName = f.getText().toString();
        EditText l = findViewById(R.id.lastName);
        lastName = l.getText().toString();
        EditText e = findViewById(R.id.email);
        email = e.getText().toString();
        EditText p = findViewById(R.id.password);
        password = p.getText().toString();


         if (isValidEmail(email))
             goodEmail = true;

        if (password.length() >= 8)
            goodPassword = true;

        if (goodPassword && goodEmail) {

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            Intent intent = new Intent(this, MainActivity.class);

            Call<List<User>> call = userAPI.getUsers() ;
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        //Toast.makeText(signUp.this, "EMPTY DATABASE...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<User> users = response.body() ;
                    assert users != null;
                    for (User user : users) {
                        if ( user.getEmail().equals(email) ) {
                            Toast.makeText(signUp.this, "" +
                                    "ERROR: USER ALREADY EXISTS!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE,"ERROR!",t) ;
                }
            });
            userAPI.save(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(signUp.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                                //return;
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(signUp.this, "ERROR!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(signUp.class.getName()).log(Level.SEVERE, "ERROR!", t);
                        }
                    });
        }
        else {
            Toast.makeText(this, "EMAIL OR PASSWORD INCORRECT \n PASSWORD MUST CONTAIN 8 CHARACTERS", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
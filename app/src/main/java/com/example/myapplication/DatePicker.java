package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DatePicker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static int myYear, myMonth, myDayOfMonth ;
    public static String DateString ;
    private boolean dateChoosed = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        Button button = (Button) findViewById(R.id.dateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }


    /**
     *
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with
     *              {@link Calendar#MONTH})
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month+1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date  = c.get(Calendar.DAY_OF_MONTH) +"/"
                +c.get(Calendar.MONTH) +"/" +c.get(Calendar.YEAR) ;
        DateString = date.toString() ;
        myYear = year ;
        myMonth = month+1 ;
        myDayOfMonth = dayOfMonth ;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(DateString);
        dateChoosed = true ;
    }

    /**
     * Check the date is right
     * @param v for xml
     */
    public void nextButtonPress (View v) {

        if (!dateChoosed) {
            Toast.makeText(this, "CHOOSE DATE!", Toast.LENGTH_LONG).show();
        }
        else {
            System.out.println(DateString);
            Intent intent = new Intent(this, TripDetails.class);
            startActivity(intent);
        }
    }
}
package com.shaybrow.taskmaster1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static  String TAG = "shayapp.main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor prefEditor = pref.edit();

        String username = pref.getString("username", null);
        if (username != null) ((TextView)findViewById(R.id.usernameDisplay)).setText(String.format(Locale.ENGLISH, username + "'s tasks"));

// button onclick
//        get by id
//        add listener
//        callback
//        dostuff

        Button potatoAddTask = findViewById(R.id.addTask);
//        returns the type of the thing that it finds

        potatoAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "This is how we long in 2021");
                Intent goToAddTasks = new Intent(MainActivity.this, addTask.class);
//                where we're coming from, where we are going
                startActivity(goToAddTasks);



            }
        });


        Button allTasks = findViewById(R.id.allTasks);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasks = new Intent(MainActivity.this, allTasks.class );
                startActivity(goToAllTasks);
            }
        });

        Button userProfile = findViewById(R.id.userProfileButton);
        userProfile.setOnClickListener(view ->{
            Intent goToUserProfile = new Intent(this, UserProfile.class);
            startActivity(goToUserProfile);
        });

        Button getAJob = findViewById(R.id.getAJob);
        getAJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGetAJob = new Intent(MainActivity.this, TaskDetails.class);
                String title = ((Button)findViewById(R.id.getAJob)).getText().toString();
                goToGetAJob.putExtra("taskTitle", title);
                startActivity(goToGetAJob);



            }
        });
        Button mealPrep = findViewById(R.id.mealPrep);
        mealPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMealPrep = new Intent(MainActivity.this, TaskDetails.class);
                String title = ((Button)findViewById(R.id.mealPrep)).getText().toString();
                goMealPrep.putExtra("taskTitle", title);
                startActivity(goMealPrep);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

    }
}
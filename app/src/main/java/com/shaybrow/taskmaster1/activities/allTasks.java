package com.shaybrow.taskmaster1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shaybrow.taskmaster1.R;

public class allTasks extends AppCompatActivity {

    public static  String TAG = "shayapp.main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        Intent intent = getIntent();
        if (intent.getStringExtra("taskTitle") == null){
            Log.i(TAG, "onCreate: don't do nothing");
        } else {
            String title = intent.getStringExtra("taskTitle");
            String body = intent.getStringExtra("taskBody");
        }

    }
}
package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent loadIntent = getIntent();
        ((TextView)findViewById(R.id.importTaskTitle)).setText(loadIntent.getStringExtra("taskTitle"));
    }
}
package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Locale;

public class addTask extends AppCompatActivity {
    PopupWindow popUp;

    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        popUp = new PopupWindow(this);
//        TextView textView = new TextView(this);
        Button button = findViewById(R.id.submitTask);
        button.setOnClickListener( view -> {
            count ++ ;
//            String title = ((EditText)findViewById(R.id.taskTitle)).getText().toString();
//            String body = ((EditText)findViewById(R.id.taskBody)).getText().toString();
            ((TextView)findViewById(R.id.submitted)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));


//            Intent intent = new Intent(addTask.this, allTasks.class);
//            intent.putExtra("taskTitle", title);
//            intent.putExtra("taskBody", body);
//            startActivity(intent);


        });
    }
    @Override
    protected void onResume (){
        super.onResume();
        ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
    }
}
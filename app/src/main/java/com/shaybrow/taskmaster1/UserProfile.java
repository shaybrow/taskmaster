package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = pref.edit();

        String username = pref.getString("username", "Guest");

        findViewById(R.id.usernameSubmit).setOnClickListener(view ->{
            String username1 = ((TextView)findViewById(R.id.usernameInput)).getText().toString();
            prefEditor.putString("username", username1);
            prefEditor.apply();
            ((TextView) findViewById(R.id.usernameInput)).setText(username1);
        });
        Button goHome = findViewById(R.id.returnHome);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfile.this, MainActivity.class);
//                where we're coming from, where we are going
                startActivity(intent);



            }
        });


    }
}
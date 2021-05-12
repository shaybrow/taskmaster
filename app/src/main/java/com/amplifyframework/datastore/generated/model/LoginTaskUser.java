package com.amplifyframework.datastore.generated.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.shaybrow.taskmaster1.MainActivity;
import com.shaybrow.taskmaster1.R;

public class LoginTaskUser extends AppCompatActivity {
    Handler loginHandler;
String TAG = "login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_task_user);
        loginHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    Intent i = new Intent(LoginTaskUser.this, MainActivity.class);
                    startActivity(i);

                }
                if (message.what == 0) {
                    Toast.makeText(LoginTaskUser.this, "Please try again", Toast.LENGTH_LONG).show();
                }
            }
        };


        Button login = findViewById(R.id.buttonLoginSubmit);
        login.setOnClickListener(view -> {
            String email = ((EditText) findViewById(R.id.loginEmail)).getText().toString();
            String pass = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
            Amplify.Auth.signIn(email, pass,
                    r -> {
                        loginHandler.sendEmptyMessage(1);
                    },
                    r -> {
                        Log.i(TAG, "onCreate: Signin failed" );
                        loginHandler.sendEmptyMessage(0);
                    }
            );

        });


    }
}
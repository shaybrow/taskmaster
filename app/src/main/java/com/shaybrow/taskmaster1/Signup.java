package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class Signup extends AppCompatActivity {
String TAG = "shaybrow.signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ((Button) findViewById(R.id.signupButton)).setOnClickListener( v ->{
            String email =((EditText) findViewById(R.id.signupEmail)).getText().toString();
            String pass = ((EditText) findViewById(R.id.signupPassword)).getText().toString();
            Amplify.Auth.signUp(
                    email, pass,
                    AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email)
//                            .userAttribute(AuthUserAttributeKey.nickname(), "shaybrow")

                            .build(), r-> {
                        Intent i = new Intent(Signup.this, SignupConfirmation.class);
                        i.putExtra("email", email);
                        i.putExtra("password", pass);
                        Log.i(TAG, "onCreate: "+ r);

                    },
                    r-> {
                        Log.i(TAG, "onCreate: "+ r);
                    }

            );

        });

    }
}
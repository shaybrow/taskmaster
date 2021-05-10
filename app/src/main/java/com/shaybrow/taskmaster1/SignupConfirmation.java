package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class SignupConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirmation);

        String email = getIntent().getStringExtra("email");

        ((Button) findViewById(R.id.buttonRegister)).setOnClickListener(v ->{
            String confirmationCode = ((EditText) findViewById(R.id.confirmationNumber)).getText().toString();
            Amplify.Auth.confirmSignUp(email,
                    confirmationCode,
                    r ->{
                    startActivity(new Intent(SignupConfirmation.this, MainActivity.class));
                    },
                    r ->{
                        Toast.makeText(SignupConfirmation.this, "confirmation code failed", Toast.LENGTH_LONG);
                    }
                    );
        });
    }
}
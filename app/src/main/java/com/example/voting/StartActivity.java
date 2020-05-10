package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button buttonRegistration;
    Button buttonAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        buttonRegistration = findViewById(R.id.button_registration);
        buttonAuthorization = findViewById(R.id.button_authorization);

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StartActivity.this, RegistrationActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });

        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StartActivity.this, AuthorizationActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });
    }
}

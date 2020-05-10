package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kenai.jffi.Main;

public class AuthorizationActivity extends AppCompatActivity {

    Button buttonAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        buttonAuthorization = findViewById(R.id.button_authorization);

        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AuthorizationActivity.this,  MainActivity.class);
                AuthorizationActivity.this.startActivity(myIntent);
            }
        });
    }
}

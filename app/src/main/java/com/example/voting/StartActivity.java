package com.example.voting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    Button buttonRegistration;
    Button buttonAuthorization;
    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressbar=findViewById(R.id.progressBar);
        constraintLayout=findViewById(R.id.startActivityContent);

        buttonRegistration = findViewById(R.id.button_registration);
        buttonAuthorization = findViewById(R.id.button_authorization);

        if(VoteApplication.getInstance().auth.getCurrentUser()!=null) {
            Intent myIntent = new Intent(StartActivity.this, MainActivity.class);
            StartActivity.this.startActivity(myIntent);
        }
        else{
            progressbar.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent myIntent = new Intent(StartActivity.this, RegistrationActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });

        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent myIntent = new Intent(StartActivity.this, AuthorizationActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });


    }


}

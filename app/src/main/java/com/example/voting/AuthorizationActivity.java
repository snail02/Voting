package com.example.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kenai.jffi.Main;

public class AuthorizationActivity extends AppCompatActivity {

    Button buttonAuthorization;
    EditText email;
    EditText pass;



    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        auth = VoteApplication.getInstance().auth;
        database = VoteApplication.getInstance().database;
        users = database.getReference("Users");
        buttonAuthorization = findViewById(R.id.button_authorization);
        email = findViewById(R.id.editTextEmailSignIn);
        pass = findViewById(R.id.editTextPassSignIn);


        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData().equals("successful")){
                    signIn();
                }
                else {
                    Toast.makeText(AuthorizationActivity.this, checkData(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void signIn(){
        auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent myIntent = new Intent(AuthorizationActivity.this,  MainActivity.class);
                        AuthorizationActivity.this.startActivity(myIntent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthorizationActivity.this, "Вы ввели неправильный email/пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String checkData(){

        if(TextUtils.isEmpty(email.getText().toString())){
            return "Введите вашу почту";
        }

        if(TextUtils.isEmpty(pass.getText().toString())){
            return "Введите ваш пароль";
        }

        return "successful";
    }


}
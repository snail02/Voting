package com.example.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    Button buttonRegistration;
    EditText fam;
    EditText name;
    EditText pat;
    EditText email;
    EditText phone;
    EditText pass;
    EditText confirmPass;

    EditText publicKey;
    EditText privateKey;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonRegistration = findViewById(R.id.button_registration);
        fam = findViewById(R.id.editTextFam);
        name = findViewById(R.id.editTextName);
        pat = findViewById(R.id.editTextPat);
        email = findViewById(R.id.editTextEmail);
        phone = findViewById(R.id.editTextPhone);
        pass = findViewById(R.id.editTextPass);
        confirmPass = findViewById(R.id.editTextConfirmPass);

        publicKey = findViewById(R.id.editTextPublicKey);
        privateKey = findViewById(R.id.editTextPrivateKey);

        auth = VoteApplication.getInstance().auth;
        database = VoteApplication.getInstance().database;
        users = VoteApplication.getInstance().users;

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkData().equals("successful")) {
                    //Регистрация
                    registration();
                    setPublicAndPrivateKey();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, checkData(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registration(){
        auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User user = new User();
                user.setFam(fam.getText().toString());
                user.setName(name.getText().toString());
                user.setPat(pat.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setPass(pass.getText().toString());

                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegistrationActivity.this, "Пользователь добавлен", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                            RegistrationActivity.this.startActivity(myIntent);
                        });
            }
        });

    }

    public void setPublicAndPrivateKey(){
        VoteApplication.getInstance().savePrivateAndPublicKey(privateKey.getText().toString(),publicKey.getText().toString());
    }

    public String checkData(){
        if(TextUtils.isEmpty(email.getText().toString())){

            return "Введите вашу почту";
        }

        if(TextUtils.isEmpty(phone.getText().toString())){

            return "Введите ваш номер телефона";
        }

        if(TextUtils.isEmpty(fam.getText().toString())){

            return "Введите фамилию";
        }

        if(TextUtils.isEmpty(name.getText().toString())){

            return "Введите имя";
        }

        if(TextUtils.isEmpty(pat.getText().toString())){

            return "Введите отчество";
        }

        if(pass.getText().toString().length()<5){

            return "Введите пароль, который более 5 символов";
        }

        if(pass.getText().equals(confirmPass.getText())){

            return pass.getText().toString() + "" + confirmPass.getText().toString();
        }

        return "successful";

    }

/*    public void signin(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(RegistrationActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(RegistrationActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}
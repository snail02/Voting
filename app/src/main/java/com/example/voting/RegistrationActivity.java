package com.example.voting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.santalu.maskedittext.MaskEditText;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    Button buttonRegistration;
    EditText fam;
    EditText name;
    EditText pat;
    EditText email;
    MaskEditText phone;
    EditText pass;
    EditText confirmPass;

    EditText publicKey;
    EditText privateKey;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;

    LinearLayout linearLayout;
    ProgressBar progressBar;

    private long mLastClickTime = 0;

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

        linearLayout=findViewById(R.id.linerLayoutRegistration);
        progressBar=findViewById(R.id.progressBarRegistration);

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(checkData().equals("successful")) {
                    //Регистрация
                    registration();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, checkData(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registration(){
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User user = new User();
                user.setFam(fam.getText().toString());
                user.setName(name.getText().toString());
                user.setPat(pat.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone("+7" + phone.getRawText());
                user.setPass(pass.getText().toString());
                user.setPrivateKey(privateKey.getText().toString());
                user.setPublicKey(publicKey.getText().toString());
                user.setFCMtoken(FirebaseInstanceId.getInstance().getToken());

                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegistrationActivity.this, "Вы успешно зарегистрированы", Toast.LENGTH_SHORT).show();

                            FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                                    .getHttpsCallable("registrationUser")
                                    .call();

                            Intent myIntent = new Intent(RegistrationActivity.this, BaseActivity.class);
                            RegistrationActivity.this.startActivity(myIntent);

                            Map<String, String> params = new HashMap<>();
                            params.put("active", "Регистрация в приложении");
                            FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                                    .getHttpsCallable("saveActiveUser")
                                    .call(params);

                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(RegistrationActivity.this, "Ошибка в регистрации", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String checkData(){

        String passStr = pass.getText().toString();
        String confStr = confirmPass.getText().toString();
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

        if(!passStr.equals(confStr)){

            return "Пароли на совпадают";
        }

        if(TextUtils.isEmpty(publicKey.getText().toString())){

            return "Введите PublicKey";
        }

        if(TextUtils.isEmpty(privateKey.getText().toString())){

            return "Введите PrivateKey";
        }

        return "successful";

    }

}
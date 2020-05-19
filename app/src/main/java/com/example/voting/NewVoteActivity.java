package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import com.example.voting.contract.Vote;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jrizani.jrspinner.JRSpinner;

public class NewVoteActivity extends AppCompatActivity {

    ArrayList<User> listUser = new ArrayList<>();  // список пользователей (кроме текущего аккаунту)
    ArrayList<String> listUserInfo = new ArrayList<>(); // список фио пользователей (кроме текущего аккаунту)
    ArrayList<String> listUserAddress = new ArrayList<>(); // список public key пользователей (кроме текущего аккаунту)
    ArrayList<String> listSelectedAddress = new ArrayList<>(); // адреса выбранные пользователей
    ArrayList<String> listSelectedFullName = new ArrayList<>(); // ФИО выбранных пользователей


    TextView nameVote;
    TextView descVote;
    Button createVote;
    Switch allUsersSwitch;

    private long mLastClickTime = 0;

    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    TextView test;

    Web3j web3j;
    Vote vote;

    Credentials credentials;

    private JRSpinner mJRSpinner;

    ArrayList<String> variant = new ArrayList<String>();

    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    static ContractGasProvider contractGasProvider = new ContractGasProvider() {
        @Override
        public BigInteger getGasPrice(String contractFunc) {
            return GAS_PRICE;
        }

        @Override
        public BigInteger getGasPrice() {
            return GAS_PRICE;
        }

        @Override
        public BigInteger getGasLimit(String contractFunc) {
            return GAS_LIMIT;
        }

        @Override
        public BigInteger getGasLimit() {
            return GAS_LIMIT;
        }
    };

    static Vote deploy(Credentials credentials, Web3j w3, String name, String desc, ArrayList<String> list) throws Exception {
        return Vote.deploy(w3, credentials, contractGasProvider, name, desc, list).send();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vote);

        nameVote = findViewById(R.id.edit_name_vote);
        descVote = findViewById(R.id.edit_desc_vote);
        createVote = findViewById(R.id.button_new_vote);

        allUsersSwitch = findViewById(R.id.switchAllUsers);

        progressbar = findViewById(R.id.progressBarNewVote);
        constraintLayout = findViewById(R.id.newVoteActivityContent);

        variant.add("Da");
        variant.add("Net");
        variant.add("Vozderjalsya");

        credentials = Credentials.create(VoteApplication.getInstance().PRIVATE_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Создание голосования");

        web3j = VoteApplication.getInstance().getWeb3j();

        mJRSpinner = findViewById(R.id.mySpinner);


        getAllUsers(); //получение списка пользователей

        mJRSpinner.setOnSelectMultipleListener(new JRSpinner.OnSelectMultipleListener() {
            @Override
            public void onMultipleSelected(List<Integer> selectedPosition) {
                //Log.d("mytest", "position " + selectedPosition);
                //Log.d("mytest", "selectedUsers " + selectedUsers(listUserInfo, selectedPosition));
                listSelectedAddress = selectedUsers(listUserAddress, selectedPosition);
                //Log.d("mytest", "selectedUsersAddress " + listSelectedAddress);
                listSelectedFullName = selectedUsers(listUserInfo, selectedPosition);
            }
        });


        createVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String name = nameVote.getText().toString();
                        String desc = descVote.getText().toString();

                        try {
                            vote = deploy(credentials, web3j, name, desc, variant);
                            if(!listSelectedAddress.isEmpty()) {
                                vote.giveRightToVote(listSelectedAddress).send();
                            }

                            Log.d("mytest", "---CREATE NEW VOTE---");
                            Log.d("mytest", "User with right " + listSelectedFullName);
                            Log.d("mytest", "vote address " + vote.getContractAddress());
                            Log.d("mytest", "totalVoters " + vote.getTotalVoters().send());
                            Log.d("mytest", "currentVoters " + vote.getCurrentVoters().send());
                            Log.d("mytest", "getMyVote() " + vote.getMyVote().send());

                            SmartContract contract = new SmartContract(vote.getContractAddress(), name, desc);

                            DatabaseReference myRef = VoteApplication.getInstance().myRef;
                            myRef.push().setValue(contract);

                        } catch (Exception e) {
                            Log.d("mytest", e.getMessage());
                        }
                    }
                });
                thread.start();

                Intent myIntent = new Intent(NewVoteActivity.this, MainActivity.class);
                NewVoteActivity.this.startActivity(myIntent);
            }
        });

        allUsersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!listUserAddress.isEmpty()){
                        listSelectedAddress=listUserAddress;
                    }
                    mJRSpinner.setVisibility(View.GONE);
                } else {
                    if (!listUserInfo.isEmpty()) {
                        String[] arrayString = listUserInfo.toArray(new String[0]);
                        mJRSpinner.setItems(arrayString);
                        mJRSpinner.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public ArrayList<String> selectedUsers(ArrayList<String> users, List<Integer> selectedPosition) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < selectedPosition.size(); i++) {
            result.add(users.get(selectedPosition.get(i).intValue()));
        }
        return result;
    }

    public void getAllUsers() {
        Thread thread = new Thread(new Runnable() {
            DatabaseReference myRef = VoteApplication.getInstance().users;
            Query myQuery = myRef;

            @Override
            public void run() {
                try {
                    myQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            User user = dataSnapshot.getValue(User.class);
                            if(!VoteApplication.getInstance().auth.getCurrentUser().getUid().equals(dataSnapshot.getKey())) {
                                listUser.add(user);
                                listUserInfo.add(user.getInfo());
                                listUserAddress.add(user.getPublicKey());
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

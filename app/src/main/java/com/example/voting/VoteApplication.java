package com.example.voting;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.util.Log;
import android.view.View;

import com.example.voting.contract.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;

public class VoteApplication extends Application {
    FirebaseDatabase database;
    DatabaseReference myRef ;
    FirebaseAuth auth;
    DatabaseReference users;
    //String connectUrl = "HTTP://192.168.0.112:7545";
    String connectUrl = "HTTP://77.79.149.186:7545";
    String PRIVATE_KEY;
    User user = new User();
    UserInfoListener listener;

    ArrayList<String> variant = new ArrayList<>();


    public final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    public final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    public ContractGasProvider contractGasProvider = new ContractGasProvider() {
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

    private Web3j web3j;

    private static VoteApplication instance;

    public static VoteApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        createVariant(); //список вариантов ответов

        web3j = connect();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        myRef = database.getReference("SmartContract");
        getUserFromFB();



    }
    public void setListener(UserInfoListener userInfoListener){
        listener=userInfoListener;
    }

    public Web3j connect(){
        web3j = Web3j.build(new HttpService(connectUrl));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            String web3ClientVersionString = clientVersion.getWeb3ClientVersion();

            if (!clientVersion.hasError()) {
                Log.d("mytest","Connected web3ClientVersion = " + web3ClientVersionString);
            } else {
                Log.d("mytest","No Connected");
            }
        } catch (Exception e) {
            Log.d("mytest","Connect error: " + e.getMessage());
        }
        return web3j;
    }

    public Web3j getWeb3j(){
        return web3j;
    }

    public void getUserFromFB() {
        if (auth.getCurrentUser() != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DatabaseReference mDatabase = users;
                        mDatabase.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        user = dataSnapshot.getValue(User.class);
                                        PRIVATE_KEY = user.getPrivateKey();
                                        if(user.isSecretary()==true)
                                            Log.d("mytest","secretary true" );
                                        else
                                            Log.d("mytest","secretary false" );
                                        Log.d("mytest","name " + user.getName());
                                        listener.onInfoLoaded(user);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        else{
        }
    }


    public void createVariant(){
        variant.add("За");
        variant.add("Против");
        variant.add("Воздержался");
    }
}



interface UserInfoListener{
    void onInfoLoaded(User user);
}


package com.example.voting;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.voting.contract.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class VoteApplication extends Application {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    DatabaseReference users;
    //String connectUrl = "HTTP://192.168.0.103:7545";
    String connectUrl = "HTTP://77.79.157.40:7545";
    //String connectUrl = "HTTP://77.79.157.40:30303";
    //String connectUrl = "http://localhost:8545";
    //String connectUrl = "https://mainnet.infura.io/v3/6217c9661e8143cdad94007434e30c43";
    String PRIVATE_KEY;
    User user = new User();
    UserInfoListener listener;
    int totalUsers;

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

    public static VoteApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupBouncyCastle();

        createVariant(); //список вариантов ответов

        web3j = connect();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        myRef = database.getReference("SmartContract");
        getUserFromFB();


    }

    public void setListener(UserInfoListener userInfoListener) {
        listener = userInfoListener;
    }

    public Web3j connect() {
        web3j = Web3j.build(new HttpService(connectUrl));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            String web3ClientVersionString = clientVersion.getWeb3ClientVersion();



            if (!clientVersion.hasError()) {
                Log.d("mytest", "Connected web3ClientVersion = " + web3ClientVersionString);
            } else {
                Log.d("mytest", "No Connected");
            }
        } catch (Exception e) {
            Log.d("mytest", "Connect error: " + e.getMessage());

        }

        return web3j;
    }


    public Web3j getWeb3j() {
        return web3j;
    }

    public void getUserFromFB() {


        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalUsers = (int) (dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                                        if (user.isSecretary() == true)
                                            Log.d("mytest", "secretary true");
                                        else
                                            Log.d("mytest", "secretary false");
                                        Log.d("mytest", "name " + user.getName());
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
        } else {
        }
    }


    public void createVariant() {
        variant.add("За");
        variant.add("Против");
        variant.add("Воздержался");
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
}


interface UserInfoListener {
    void onInfoLoaded(User user);
}



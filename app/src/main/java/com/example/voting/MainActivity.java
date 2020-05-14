package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import jnr.ffi.annotations.In;

public class MainActivity extends AppCompatActivity {


    List<VotingCard> listCard = new ArrayList<>();

    ViewPager viewPager;
    TabLayout tabLayout;
    MyFragmetPagerAdapter pagerAdapter;


    //String connectUrl = "https://ropsten.infura.io/v3/6217c9661e8143cdad94007434e30c43";
    Web3j web3j;
    Vote vote;
    private final static String PRIVATE_KEY = "243f16f0f8e5ba62faa8405324087d50a70e5a7f96081f8b5cfe585baf9984b3";


    /*public Web3j connect(String url){
         web3j = Web3j.build(new HttpService(url));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            String web3ClientVersionString = clientVersion.getWeb3ClientVersion();

            if (!clientVersion.hasError()) {
                Log.d("mytest","Connected web3ClientVersion = " + web3ClientVersionString);
            } else {
                Log.d("mytest","No Connected");
            }
        } catch (Exception e) {
            Log.d("mytest","Error " + e.getMessage());
        }

        return web3j;
    }*/



    static BigInteger getGasPrice(Web3j w3) throws IOException {
        EthGasPrice ethGasPrice = w3.ethGasPrice().send();
        return ethGasPrice.getGasPrice();

    }

  /*  static Vote deploy(Credentials credentials, Web3j w3) throws Exception {
        String nameQ = "nameQ";
        String descQ = "descQ";
        ArrayList<String> list = new ArrayList<>();
        list.add("da");
        list.add("net");
        return Vote.deploy(w3, credentials, contractGasProvider, nameQ, descQ, list).send();
    }

*/

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

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                Toast.makeText(MainActivity.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, StartActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBouncyCastle();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Голосования");

        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        pagerAdapter = new MyFragmetPagerAdapter(getSupportFragmentManager());

      //  VotingCard vc = new VotingCard();
     //   listCard.add(vc);

     //   pagerAdapter.activeVotes(listCard);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




        //connect(connectUrl);
        web3j = VoteApplication.getInstance().getWeb3j();


       /* listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);*/


        DatabaseReference myRef = VoteApplication.getInstance().myRef;
       // DatabaseReference myRef = VoteApplication.getInstance().database;
        Query myQuery = myRef;







        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getName(), contract.getDescription(), contract.getAddress());
                ArrayList<VotingCard> list2 = new ArrayList<>();
                list2.add(vc);
                pagerAdapter.activeVotes(list2);

                //adapterCard.addCard(vc);
               // recyclerVotingCard.setAdapter(adapterCard);




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







    }
}

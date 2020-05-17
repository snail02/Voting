package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.protocol.Web3j;


import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    MyFragmetPagerAdapter pagerAdapter;

    VotingCardAdapter cardHistory;
    VotingCardAdapter cardActive;

    Web3j web3j;

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

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        web3j = VoteApplication.getInstance().getWeb3j();

        DatabaseReference myRef = VoteApplication.getInstance().myRef;
        Query myQuery = myRef;

        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getName(), contract.getDescription(), contract.getAddress(), contract.isStatusActive(), dataSnapshot.getKey());
                ArrayList<VotingCard> listActive = new ArrayList<>();
                ArrayList<VotingCard> listPassive = new ArrayList<>();
                if(vc.isStatusActive()){
                    listActive.add(0, vc);
                    pagerAdapter.activeVotes(listActive);
                }
                else{
                    listPassive.add(0, vc);
                    pagerAdapter.historyVotes(listPassive);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getName(), contract.getDescription(), contract.getAddress(), contract.isStatusActive(), dataSnapshot.getKey());
                ArrayList<VotingCard> listActive = new ArrayList<>();
                ArrayList<VotingCard> listPassive = new ArrayList<>();
                if(vc.isStatusActive()){
                    listActive.add(0,vc);
                    pagerAdapter.activeVotes(listActive);
                }
                else{
                    listPassive.add(0, vc);
                    pagerAdapter.historyVotes(listPassive);
                }
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

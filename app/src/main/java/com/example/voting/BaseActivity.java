package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    MainFragmentAdapter pagerAdapter;

    Web3j web3j;

    Credentials credentials;
    Vote vote;


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
                Toast.makeText(BaseActivity.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Map<String, String> params = new HashMap<>();
        params.put("active", "Выход из аккаунта");

        Context context = this;
        FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                .getHttpsCallable("saveActiveUser")
                .call(params)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        VoteApplication.getInstance().users.child(VoteApplication.getInstance().auth.getCurrentUser().getUid()).child("fcmtoken").setValue("");

                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(context, StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setupBouncyCastle();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        pagerAdapter = new MainFragmentAdapter(getSupportFragmentManager());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Активные голосования");


        viewPager.setAdapter(pagerAdapter);

        web3j = VoteApplication.getInstance().getWeb3j();

        bottomNavigationView.setSelectedItemId(R.id.action_votes);
        viewPager.setCurrentItem(1, false);

/*
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


*/
        //credentials = Credentials.create(VoteApplication.getInstance().PRIVATE_KEY);



        DatabaseReference myRef = VoteApplication.getInstance().myRef;
        Query myQuery = myRef;

        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getName(), contract.getDescription(), contract.getAddress(), contract.isStatusActive(), dataSnapshot.getKey(), contract.getTimelife());
                ArrayList<VotingCard> listActive = new ArrayList<>();
                ArrayList<VotingCard> listPassive = new ArrayList<>();
                if (vc.isStatusActive()) {
                    listActive.add(0, vc);

                    pagerAdapter.activeVotes(listActive);


                } else {
                    listPassive.add(0, vc);
                    pagerAdapter.historyVotes(listPassive);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getName(), contract.getDescription(), contract.getAddress(), contract.isStatusActive(), dataSnapshot.getKey(), contract.getTimelife());
                ArrayList<VotingCard> listActive = new ArrayList<>();
                ArrayList<VotingCard> listPassive = new ArrayList<>();
                if (vc.isStatusActive()) {
                    listActive.add(0, vc);
                    pagerAdapter.activeVotes(listActive);
                } else {
                    listPassive.add(0, vc);
                    pagerAdapter.activeVotesRemove(vc);
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.action_history);
                        getSupportActionBar().setTitle("История действий");

                        FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                                .getHttpsCallable("getActiveUser")
                                .call().addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                            @Override
                            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                Log.d("mytest",httpsCallableResult.getData().toString());
                                HashMap<String, ArrayList<Object>> map = (HashMap<String, ArrayList<Object>>) httpsCallableResult.getData();
                                ArrayList<Object> list = map.get("active");
                                ArrayList<ActiveItem> list2 = new ArrayList<>() ;
                                for(int i=0; i<list.size(); i++){

                                    long sec = ((HashMap<String,Integer>)((HashMap<String, Object>)list.get(i)).get("date")).get("_seconds") ;
                                    long milisec = sec*1000;
                                    Date date = new Date(milisec);

                                    String active = ((HashMap<String,String>)list.get(i)).get("active");
                                    ActiveItem activeItem = new ActiveItem(date, active);
                                    list2.add(activeItem);

                                }
                                pagerAdapter.actionHistory(list2);

                                //adapterActionHistory.addActionItems(list2);




                            }
                        });

                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.action_votes);
                        getSupportActionBar().setTitle("Активные голосования");
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.action_votes_closed);
                        getSupportActionBar().setTitle("Завершенные голосования");
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.action_profile);
                        getSupportActionBar().setTitle("Профиль");
                        break;
                }
                //viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_history:
                        viewPager.setCurrentItem(0, false);
                        getSupportActionBar().setTitle("История действий");
                        break;
                    case R.id.action_votes:
                        viewPager.setCurrentItem(1, false);
                        getSupportActionBar().setTitle("Активные голосования");
                        break;
                    case R.id.action_votes_closed:
                        viewPager.setCurrentItem(2, false);
                        getSupportActionBar().setTitle("Завершенные голосования");
                        break;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(3, false);
                        getSupportActionBar().setTitle("Профиль");
                        break;
                }
                return true;
            }
        });


    }
}

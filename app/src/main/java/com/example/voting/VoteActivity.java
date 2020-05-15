package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.web3j.crypto.Credentials;
import org.web3j.tuples.Tuple;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class VoteActivity extends AppCompatActivity {

    TextView nameVote;
    TextView descVote;
    TextView messageForUser;
    Button yes;
    Button no;
    Button neutral;
    String address;
    String idCard;

    boolean isActive = true;
    boolean isLastVoters = false;
    DatabaseReference database;

    Vote vote;
    Credentials credentials;
    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Голосование");

        nameVote = findViewById(R.id.name_vote_text);
        descVote = findViewById(R.id.desc_vote_text);
        messageForUser = findViewById(R.id.message_for_user);
        yes = findViewById(R.id.variant_yes_button);
        no = findViewById(R.id.variant_no_button);
        neutral = findViewById(R.id.variant_neutral_button);

        progressbar = findViewById(R.id.progressBarVote);
        constraintLayout = findViewById(R.id.voteActivityContent);

        Bundle arguments = getIntent().getExtras();
        VotingCard card = (VotingCard) (arguments.getSerializable("card"));
        nameVote.setText(card.getName());
        descVote.setText(card.getDescription());


        address = card.getAddress();
        idCard = card.getId();
        isActive = card.isStatusActive();

        credentials = Credentials.create(VoteApplication.getInstance().PRIVATE_KEY);
        Log.d("mytest", "adress cred" + credentials.getAddress());

        vote = Vote.load(address, VoteApplication.getInstance().getWeb3j(), credentials, VoteApplication.getInstance().contractGasProvider);

        checkVoteInfo();
        checkVotes();


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVote(0);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVote(1);
            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVote(2);

            }
        });
    }

    public void checkVotes() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String status;
                    try {
                        status = vote.getMyVote().send();
                    }
                    catch (Exception e){
                        status = "Вы не можете голосовать";

                    }
                    Log.d("mytest", "status " + status);
                    if (status.equals("You are not voted")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressbar.setVisibility(View.GONE);
                                    constraintLayout.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressbar.setVisibility(View.GONE);
                                    constraintLayout.setVisibility(View.GONE);

                                    if(!isActive){
                                        messageForUser.setText("Голосование закрыто");
                                    }
                                    messageForUser.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void checkVoteInfo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BigInteger total = vote.getTotalVoters().send();
                    BigInteger current = vote.getCurrentVoters().send();

                    Log.d("mytest", "---OPEN VOTE ACTIVITY---");
                    Log.d("mytest", "totalVoters " + total);
                    Log.d("mytest", "currentVoters " + current);
                    Log.d("mytest", "my vote " + vote.getMyVote().send());

                    if(total.intValue() - current.intValue() == 1){
                        isLastVoters = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void sendVote(int index) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    vote.vote(BigInteger.valueOf(index)).send();
                    closeVote();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(VoteActivity.this, "Вы проголосовали", Toast.LENGTH_SHORT).show();
                                //progressbar.setVisibility(View.VISIBLE);
                                constraintLayout.setVisibility(View.GONE);
                                messageForUser.setVisibility((View.VISIBLE));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String getResultVote(int index) throws Exception {
        return "...";
    }

    public void closeVote(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLastVoters){
                    database = FirebaseDatabase.getInstance().getReference().child("SmartContract");
                    database.child(idCard).child("statusActive").setValue(false);
                }
            }
        });
        thread.start();
    }
}

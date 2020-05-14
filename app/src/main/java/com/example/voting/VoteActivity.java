package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.contract.Vote;

import org.web3j.crypto.Credentials;

import java.math.BigInteger;

public class VoteActivity extends AppCompatActivity {

    TextView nameVote;
    TextView descVote;
    Button yes;
    Button no;
    Button neutral;
    String address;

    boolean check;


    Vote vote;
    Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Голосование");

        nameVote = findViewById(R.id.name_vote_text);
        descVote = findViewById(R.id.desc_vote_text);
        yes = findViewById(R.id.variant_yes_button);
        no = findViewById(R.id.variant_no_button);
        neutral = findViewById(R.id.variant_neutral_button);

        buttonsVisibility(false);

        Bundle arguments = getIntent().getExtras();
        VotingCard card = (VotingCard) (arguments.getSerializable("card"));
        nameVote.setText(card.getName());
        descVote.setText(card.getDescription());

        address = card.getAddress();

        credentials = Credentials.create(VoteApplication.getInstance().loadPrivateKey());
        Log.d("mytest", "adress cred" + credentials.getAddress());

        vote = Vote.load(address, VoteApplication.getInstance().getWeb3j(), credentials, VoteApplication.getInstance().contractGasProvider);

        buttonsVisibility(allCheckVoted());///////////////////////////////////////////////////////////////////////////////

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote.vote(BigInteger.valueOf(0));
                Toast.makeText(VoteActivity.this, "Вы проголосовали", Toast.LENGTH_SHORT).show();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote.vote(BigInteger.valueOf(1));
                Toast.makeText(VoteActivity.this, "Вы проголосовали", Toast.LENGTH_SHORT).show();

            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote.vote(BigInteger.valueOf(2));
                Toast.makeText(VoteActivity.this, "Вы проголосовали", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public boolean allCheckVoted() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if(vote.getMyVote().send().equals("You are not voted")){
                       check=true;
                    }
                    else {
                        check=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        thread.start();
        return check;

    }




    public void buttonsVisibility(boolean check){
        if(check) {
            yes.setVisibility(View.VISIBLE);
            no.setVisibility(View.VISIBLE);
            neutral.setVisibility(View.VISIBLE);
        }
        else
        {
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            neutral.setVisibility(View.GONE);
        }
    }


}

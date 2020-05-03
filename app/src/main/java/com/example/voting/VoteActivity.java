package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class VoteActivity extends AppCompatActivity {

    TextView nameVote;
    TextView descVote;
    Button yes;
    Button no;
    Button neutral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        nameVote = findViewById(R.id.name_vote_text);
        descVote = findViewById(R.id.desc_vote_text);
        yes = findViewById(R.id.variant_yes_button);
        no = findViewById(R.id.variant_no_button);
        neutral = findViewById(R.id.variant_neutral_button);
    }
}

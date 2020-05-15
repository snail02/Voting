package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.Printer;
import android.util.TimingLogger;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.core.PiePoint;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.voting.contract.Vote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Credentials;
import org.web3j.tuples.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    String status;

    AnyChartView chartView;

    int yesCount;
    int noCount;
    int vozderjalsya_count;

    boolean isActive = true;
    boolean isLastVoters = false;
    DatabaseReference database;

    Vote vote;
    Credentials credentials;
    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    ArrayList<Integer> countVotesForVariant = new ArrayList<>();

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

        chartView = findViewById(R.id.chartView);

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
        if (!isActive) {

            getResultVote();

            messageForUser.setText("Голосование закрыто");
            //countVotesForVariant.add(5);
            //countVotesForVariant.add(5);
            //countVotesForVariant.add(5);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressbar.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.GONE);
                        messageForUser.setVisibility(View.VISIBLE);

                        //setupPieChart(VoteApplication.getInstance().variant,countVotesForVariant);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        try {
                            status = vote.checkRight().send();
                        } catch (Exception e) {
                            e.printStackTrace();
                            status = "Вы не можете голосовать";
                        }

                        Log.d("mytest", "status " + status);
                        if (status.equals("You have right") && vote.getMyVote().send().equals("You are not voted")) {
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

                                        if (status.equals("Has no right to vote")) {
                                            messageForUser.setText("У вас нет доступа");
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

                    if (total.intValue() - current.intValue() == 1) {
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

    public void printResultVote() {
        if (!isActive) {

            for (int i = 0; i < VoteApplication.getInstance().variant.size(); i++) {
                Log.d("mytest", VoteApplication.getInstance().variant.get(i) + " " + countVotesForVariant.get(i));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setupPieChart(VoteApplication.getInstance().variant,countVotesForVariant);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // Log.d("mytest", "da " +  yesCount);
            // Log.d("mytest", "net " +  noCount);
            // Log.d("mytest", "vozderjalsya " +  vozderjalsya_count);
        }
    }

    public void getResultVote() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (int i = 0; i < VoteApplication.getInstance().variant.size(); i++) {


                        countVotesForVariant.add(vote.getCountVoteVariant(BigInteger.valueOf(i)).send().intValue());

                    }


                    //yesCount = vote.getCountVoteVariant(BigInteger.valueOf(0)).send().intValue();
                    //noCount=vote.getCountVoteVariant(BigInteger.valueOf(1)).send().intValue();
                    // vozderjalsya_count=vote.getCountVoteVariant(BigInteger.valueOf(2)).send().intValue();

                    printResultVote();



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }

    public void closeVote() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLastVoters) {
                    database = FirebaseDatabase.getInstance().getReference().child("SmartContract");
                    database.child(idCard).child("statusActive").setValue(false);
                }
            }
        });
        thread.start();
    }

    public void setupPieChart(List<String> firstList, List<Integer> secondList) {
        Pie pie = AnyChart.pie();

        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < firstList.size(); i++) {
            dataEntries.add(new ValueDataEntry(firstList.get(i), secondList.get(i)));
        }

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(chartView.getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        pie.title("Распределение голосов");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Варианты ответов")
                .padding(0d, 0d, 10d, 0d);




        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        pie.data(dataEntries);

        chartView.setChart(pie);



    }
}

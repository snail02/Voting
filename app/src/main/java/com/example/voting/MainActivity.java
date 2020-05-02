package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.voting.contract.Vote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buttonDeploy;
    RecyclerView recyclerVotingCard;
    FloatingActionButton fab;
    List<VotingCard> listCard = new ArrayList<>();

    String connectUrl = "HTTP://192.168.0.101:7545";
    Web3j web3j;
    Vote vote;
    private final static String PRIVATE_KEY = "f9dc337a66fe3bce876ba3db6e9a7f30867a64d7422b2500734483c4c1dc611e";
    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    public Web3j connect(String url){
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
    }

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

    static BigInteger getGasPrice(Web3j w3) throws IOException {
        EthGasPrice ethGasPrice = w3.ethGasPrice().send();
        return ethGasPrice.getGasPrice();

    }

    static Vote deploy(Credentials credentials, Web3j w3) throws Exception {
        String nameQ = "nameQ";
        String descQ = "descQ";
        ArrayList<String> list = new ArrayList<>();
        list.add("da");
        list.add("net");
        return Vote.deploy(w3, credentials, contractGasProvider, nameQ, descQ, list).send();
    }

    public void createNewVote(){
        Intent myIntent = new Intent(MainActivity.this, NewVoteActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDeploy = (Button) findViewById(R.id.buttonDeploy);
        fab = findViewById(R.id.create_button);

        connect(connectUrl);

        VotingCard vc = new VotingCard();
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);

        recyclerVotingCard = findViewById(R.id.list);
        VotingCardAdapter adapterCard = new VotingCardAdapter(this, listCard);
        recyclerVotingCard.setAdapter(adapterCard);

        buttonDeploy.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                          /*  Credentials credentials = Credentials.create(PRIVATE_KEY);
                             vote = deploy(credentials,web3j);
                             String address = vote.getContractAddress();
                             Log.d("mytest", "address "+ address);
                             String name = vote.getQuestionName().send();
                             Log.d("mytest", "name "+ name);
                             List<String> accounts = web3j.ethAccounts().send().getAccounts();
                            Log.d("mytest", "deployedAddress "+ accounts);
                            BigInteger answer =BigInteger.valueOf(1);
                            Log.d("mytest", "getMyVote "+ vote.getMyVote().send());
                            vote.vote(answer);
                            Log.d("mytest", "getMyVote "+ vote.getMyVote().send());
                            String winnnerName;
                            winnnerName = vote.winnerName().send();
                            Log.d("mytest", "winnnerName "+ winnnerName);
                            Log.d("mytest", "getQuestionDescription "+ vote.getQuestionDescription().send());
                            Log.d("mytest", "secretary "+ vote.secretary().send());
                            Log.d("mytest", "credentials.getAddress() "+  credentials.getAddress());*/
                            //Credentials credentials = Credentials.create("f9dc337a66fe3bce876ba3db6e9a7f30867a64d7422b2500734483c4c1dc611e");
                            //vote = deploy(credentials,web3j);
                            Credentials credentials2 = Credentials.create("9980e7943cffbeddbd64566c6d1ca47ac400ce0d9343d89e8ac2536da75a3c77");
                            Vote vote = Vote.load("0x1fc1b46aedf227d3532426b2157e30a608b19477",web3j,credentials2,contractGasProvider);
                            ArrayList<String> address = new ArrayList<>();
                            address.add("0x21a2CEf207A3e0790243903867Bd81A77527232f");
                            try {
                               // Log.d("mytest", "give " + vote.giveRightToVote(address).send());
                            }
                            catch (Exception e) {
                                Log.d("mytest",e.getMessage());
                            }

                            Log.d("mytest", "vote address " + vote.getContractAddress());
                            Log.d("mytest", "getMyVote() " + vote.getMyVote().send());
                            Log.d("mytest", "getTotalVoters() " + vote.getTotalVoters().send());
                            Log.d("mytest", "getCurrentVoters() " + vote.getCurrentVoters().send());
                            try {
                                Log.d("mytest", "vote " + vote.vote(BigInteger.valueOf(1)).send());
                            }
                            catch (Exception e){
                                Log.d("mytest", e.getMessage());
                            }
                            Log.d("mytest", "getMyVote() " + vote.getMyVote().send());
                            Log.d("mytest", "getCurrentVoters() " + vote.getCurrentVoters().send());

                            //Log.d("mytest", "getMyVote() " + vote2.get);


                           // try {
                                 //пррпрпрпррп vote2.giveRightToVote("0x7643a332bA53aB63F16993469517bEF45DA6E508").send(); ++++++++++
                            //    Log.d("mytest", "vote  " + vote2.vote(BigInteger.valueOf(1)).send());
                           // }
                           // catch (Exception e){
                            //    Log.d("mytest",e.getMessage());
                          //  }
                           // Log.d("mytest", "getMyVote() " + vote2.getMyVote().send());
                           // Log.d("mytest", "secretary() " + vote2.secretary().send());

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                createNewVote();
            }
        });


    }
}

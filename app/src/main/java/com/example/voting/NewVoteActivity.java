package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.voting.contract.Vote;
import com.google.firebase.database.DatabaseReference;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static jnr.a64asm.Asm.w3;

public class NewVoteActivity extends AppCompatActivity {

    TextView nameVote;
    TextView descVote;
    Button createVote;

    TextView test;

    Web3j web3j;
    Vote vote;

    ArrayList<String> variant = new ArrayList<String>();

    private final static String PRIVATE_KEY = "243f16f0f8e5ba62faa8405324087d50a70e5a7f96081f8b5cfe585baf9984b3";
    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
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
    Credentials credentials = Credentials.create(PRIVATE_KEY);
    ArrayList<String> address = new ArrayList<>();

    static Vote deploy(Credentials credentials, Web3j w3, String name, String desc, ArrayList<String> list) throws Exception {
        return Vote.deploy(w3, credentials, contractGasProvider, name, desc, list).send();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vote);

        nameVote = findViewById(R.id.edit_name_vote);
        descVote = findViewById(R.id.edit_desc_vote);
        createVote = findViewById(R.id.button_new_vote);
        test = findViewById(R.id.textView);

        variant.add("Da");
        variant.add("Net");
        variant.add("Vozderjalsya");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Создание голосования");

        web3j = VoteApplication.getInstance().getWeb3j();

        createVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String name = nameVote.getText().toString();
                        String desc = descVote.getText().toString();
                        //test.setText(name + "  " + desc);
                        address.add("0x87a851a5E1852eaBA8a2124AF4FdBbc0ce73c8AB");

                        try {
                            vote = deploy(credentials, web3j,name, desc, variant);
                            Log.d("mytest", "vote address " + vote.getContractAddress());
                            Log.d("mytest", "getMyVote() " + vote.getMyVote().send());

                            SmartContract contract = new SmartContract(vote.getContractAddress(), name, desc);

                            DatabaseReference myRef = VoteApplication.getInstance().myRef;
                            myRef.push().setValue(contract);
                        } catch (Exception e) {
                            Log.d("mytest", e.getMessage());
                        }
                    }
                });
                thread.start();
            }
        });
    }
}

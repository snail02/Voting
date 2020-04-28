package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.voting.contract.Vote;

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


    String connectUrl = "HTTP://192.168.0.112:7545";
    Web3j web3j;
    Vote vote;
    private final static String PRIVATE_KEY = "243f16f0f8e5ba62faa8405324087d50a70e5a7f96081f8b5cfe585baf9984b3";
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

    static Vote deploy(Credentials credentials, Web3j w3)
            throws Exception {
        String nameQ = "nameQ";
        String descQ = "descQ";
        ArrayList<String> list = new ArrayList<>();
        list.add("da");
        list.add("net");
        return Vote.deploy(w3, credentials, contractGasProvider, nameQ, descQ, list).send();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDeploy = (Button) findViewById(R.id.buttonDeploy);


        connect(connectUrl);


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
                            Credentials credentials = Credentials.create(PRIVATE_KEY);
                            Credentials credentials2 = Credentials.create("b6d59e95df7bf4f74c815c1604f7fd17e9280ecf862b121f94db1ce70dcc30a0");
                            Vote vote2 = Vote.load("0x46dd1214e20e578b326dd20fcaf3bab4a99578e7",web3j,credentials2,contractGasProvider);

                            Log.d("mytest", "vote address " + vote2.getContractAddress());
                            Log.d("mytest", "getMyVote() " + vote2.getMyVote().send());

                            try {
                                 //vote2.giveRightToVote("0x7643a332bA53aB63F16993469517bEF45DA6E508").send();
                                Log.d("mytest", "vote  " + vote2.vote(BigInteger.valueOf(1)).send());
                            }
                            catch (Exception e){
                                Log.d("mytest",e.getMessage());
                            }
                            Log.d("mytest", "getMyVote() " + vote2.getMyVote().send());
                            Log.d("mytest", "secretary() " + vote2.secretary().send());













                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });



    }
}

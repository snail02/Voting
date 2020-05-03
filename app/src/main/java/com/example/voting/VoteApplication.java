package com.example.voting;

import android.app.Application;
import android.util.Log;

import com.example.voting.contract.Vote;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class VoteApplication extends Application {

    private Web3j web3j;

    private static VoteApplication instance;

    public static VoteApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        web3j = connect();
    }

    public Web3j connect(){
        web3j = Web3j.build(new HttpService("HTTP://192.168.0.103:7545"));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            String web3ClientVersionString = clientVersion.getWeb3ClientVersion();

            if (!clientVersion.hasError()) {
                Log.d("mytest","Connected web3ClientVersion = " + web3ClientVersionString);
            } else {
                Log.d("mytest","No Connected");
            }
        } catch (Exception e) {
            Log.d("mytest","Connect error: " + e.getMessage());
        }

        return web3j;
    }

    public Web3j getWeb3j(){
        return web3j;
    }
}

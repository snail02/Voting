package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;

import java.util.ArrayList;
import java.util.List;


public class ActiveVoteFragment extends Fragment {

    Button buttonDeploy;
    RecyclerView recyclerVotingCard;
    FloatingActionButton fab;
    VotingCardAdapter adapterCard;

    String connectUrl=VoteApplication.getInstance().connectUrl;


    public ActiveVoteFragment() {
        // Required empty public constructor
    }


    public static ActiveVoteFragment newInstance() {
        ActiveVoteFragment fragment = new ActiveVoteFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_vote, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonDeploy = (Button) view.findViewById(R.id.buttonDeploy);
        fab = view.findViewById(R.id.create_button);

        recyclerVotingCard = view.findViewById(R.id.list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerVotingCard.setLayoutManager(mLayoutManager);
         adapterCard = new VotingCardAdapter(new ArrayList<>());
        recyclerVotingCard.setAdapter(adapterCard);


        buttonDeploy.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {






                            /*String password = "secr3t";
                            ECKeyPair keyPair = Keys.createEcKeyPair();
                            WalletFile wallet = Wallet.createStandard(password, keyPair);

                            Log.d("mytest","Priate key: " + keyPair.getPrivateKey().toString(16));
                            Log.d("mytest","Account: " + wallet.getAddress());
                            Log.d("mytest","Public key: " + keyPair.getPublicKey().toString(16));
                            List <String> list;
                            list = web3j.ethAccounts().send().getAccounts();
                            Log.d("mytest", list.toString());
                            Credentials credentials = Credentials.create(keyPair.getPrivateKey().toString(16));*/



                            /////////////////////
                           /* String walletPassword = "secr3t";
                            String walletDirectory = "/app/wallet";

                            String walletName = WalletUtils.generateNewWalletFile(walletPassword, new File(walletDirectory));
                            //System.out.println("wallet location: " + walletDirectory + "/" + walletName);


                            Credentials credentials = WalletUtils.loadCredentials(walletPassword, walletDirectory + "/" + walletName);

                            vote = deploy(credentials,web3j);*/

////////////////////////////////////////

                            // create a File object for the parent directory
                            // File wallpaperDirectory = new File("/path/to/destination/");
// have the object build the directory structure, if needed.
                            // wallpaperDirectory.mkdirs();
// create a File object for the output file
                            //File outputFile = new File(wallpaperDirectory, "123");
// now attach the OutputStream to the file object, instead of a String representation
                            //FileOutputStream fos = new FileOutputStream(outputFile);



                      /*     File folder = new File(getFilesDir() +
                                    File.separator + "Wallet");
                            boolean success = true;
                            if (!folder.exists()) {
                                success = folder.mkdirs();
                            }
                            if (success) {
                                // Do something on success
                            } else {
                                // Do something else on failure
                            }

                            String walletPassword = "secr3t";
                            String walletDirectory = folder.getPath();

                            String walletName = WalletUtils.generateNewWalletFile(walletPassword, new File(walletDirectory));

                            Log.d("mytest","wallet location: " + walletDirectory + "/" + walletName);


                            Credentials credentials4 = WalletUtils.loadCredentials(walletPassword, walletDirectory + "/" + walletName);



*/
                            // String fileName = WalletUtils.generateNewWalletFile( "secr3t", new File("/path/to/destination"));
/*////////////////////////////
                            try {
                                String password = "secr3t";
                                ECKeyPair keyPair = Keys.createEcKeyPair();
                                WalletFile wallet = Wallet.createStandard(password, keyPair);
                                Credentials credentials = Credentials.create(keyPair.getPrivateKey().toString(16));
                                Credentials cred1 = Credentials.create(PRIVATE_KEY);
                                vote = deploy(cred1,web3j);
                                ArrayList<String> list2 = new ArrayList<>();
                                list2.add(credentials.getAddress());
                                Log.d("mytest","List2: " + list2);
                                Log.d("mytest","TotalVoters: " +  vote.getTotalVoters().send().toString());
                                vote.giveRightToVote(list2);
                                Log.d("mytest","TotalVoters: " +  vote.getTotalVoters().send().toString());
                                Log.d("mytest","Priate key: " + keyPair.getPrivateKey().toString(16));
                                Log.d("mytest","Account: " + wallet.getAddress());
                                List<String> acclist2 =  web3j.ethAccounts().send().getAccounts();
                                Log.d("mytest", "acc "+acclist2);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
*/
///////////////////////////////////
/*

                            String c = Credentials.create(PRIVATE_KEY).getAddress();

                            String walletPassword = "secr3t";
                            Admin web3jAdmin = Admin.build(new HttpService(connectUrl));
                            try {
                                Web3ClientVersion clientVersion = web3jAdmin.web3ClientVersion().sendAsync().get();
                                String web3ClientVersionString = clientVersion.getWeb3ClientVersion();

                                if (!clientVersion.hasError()) {
                                    Log.d("mytest","Connected AdminWe3bjweb3ClientVersion = " + web3ClientVersionString);
                                } else {
                                    Log.d("mytest","No Connected");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                         //   String address = credentials4.getAddress();
                            NewAccountIdentifier newAccountIdentifier = web3jAdmin.personalNewAccount(walletPassword).send();

                            //Log.d("mytest","New account address: " + address);
                            Log.d("mytest","New account newAccountIdentifier: " + newAccountIdentifier.getAccountId());

                            List<String> acclist2 =  web3j.ethAccounts().send().getAccounts();
                            Log.d("mytest", "acc "+acclist2);
                          //  BigInteger unlockDuration = BigInteger.valueOf(60L);
                            //PersonalUnlockAccount personalUnlockAccount = web3jAdmin.personalUnlockAccount(address, walletPassword, unlockDuration).send();
                            PersonalUnlockAccount personalUnlockAccount = web3jAdmin.personalUnlockAccount(newAccountIdentifier.getAccountId(), walletPassword).send();
                            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
                            Log.d("mytest","Account unlock " + isUnlocked);


                            //String accountAddress = credentials4.getAddress();
                           // Log.d("mytest", "address " + accountAddress);
                           // vote = deploy(credentials4,web3j);
                        List<String> acclist =  web3j.ethAccounts().send().getAccounts();
                        Log.d("mytest", "acc "+acclist);

               */





                            //Log.d("mytest", "address contract" + vote.getContractAddress());
                            //Credentials credentials = Credentials.create(PRIVATE_KEY);
                            // vote = deploy(credentials,web3j);

          /*                  String walletPassword = "secr3t";
                            Admin web3jAdmin = Admin.build(new HttpService("HTTP://192.168.0.112:7545"));
                            try {
                                Web3ClientVersion clientVersion = web3jAdmin.web3ClientVersion().sendAsync().get();
                                String web3ClientVersionString = clientVersion.getWeb3ClientVersion();

                                if (!clientVersion.hasError()) {
                                    Log.d("mytest","Connected AdminWe3bjweb3ClientVersion = " + web3ClientVersionString);
                                } else {
                                    Log.d("mytest","No Connected");
                                }
                            } catch (Exception e) {
                               e.printStackTrace();
                            }


                            String password = "12345678";
                            NewAccountIdentifier newAccountIdentifier = web3jAdmin.personalNewAccount(password).send();
                            String address = newAccountIdentifier.getAccountId();

                            Log.d("mytest","New account address: " + address);
                            BigInteger unlockDuration = BigInteger.valueOf(60L);
                            try {
                                PersonalUnlockAccount personalUnlockAccount = web3jAdmin.personalUnlockAccount(address, password, unlockDuration).send();
                                Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
                                Log.d("mytest","Account unlock " + isUnlocked);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                           //List<String> list = web3j.ethAccounts().send().getAccounts();
                           List<String> list = web3jAdmin.personalListAccounts().send().getAccountIds();
                            Log.d("mytest", "list = " + list);

*/

/*
                            Credentials credentials2 = Credentials.create("11122c8a382dd4bf2886fa44a97d902c6c6eed149ddc853be18a16a4fd797ce7");
                            Vote vote = Vote.load("0x99a2e7AFFc9B98B85BE8833B47Cb2A7A663D71e4",web3j,credentials2,contractGasProvider);



                            ArrayList<String> list2 = new ArrayList<>();
                            list2.add("0xE9baf071FE33713656F6Cc4231B60EfF19d9aD89");
                            list2.add(address);
                            try {
                                 Log.d("mytest", "list2 =  " + list2);
                                 Log.d("mytest", "give " + vote.giveRightToVote(list2).send());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
*/

                            //Log.d("mytest", "NewAccId " + web3jAdmin.personalNewAccount(walletPassword).send().getAccountId());
                            // Credentials newCred =  web3jAdmin.personalNewAccount(walletPassword).send().getAccountId();


                            /*Admin web3jAdmin = Admin.build(new HttpService(connectUrl));
                            PersonalUnlockAccount personalUnlockAccount = web3jAdmin.personalUnlockAccount(accountAddress, walletPassword).send();
                            if (personalUnlockAccount.accountUnlocked()) {

                            }*/






                           /* WalletUtils.generateNewWalletFile("PASSWORD", new File("/sdcard/Wallet/"), true);
                            Credentials credentials = WalletUtils.loadCredentials("PASSWORD", "/sdcard/Wallet/");
                            vote = deploy(credentials,web3j);*/



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
                            // Credentials credentials = Credentials.create("a1866fc6b940e598e2219658949ca34797ba01b6495f507c0208a2e37c2f37c8");
                            //  vote = deploy(credentials,web3j);
                            //Credentials credentials2 = Credentials.create("457927f9a0da8e47420a66e0c2729aa28568e1cb9e35a2b6580875459671c8ab");
                            //Vote vote = Vote.load("0x1fc1b46aedf227d3532426b2157e30a608b19477",web3j,credentials2,contractGasProvider);
                            // ArrayList<String> address = new ArrayList<>();
                            // address.add("0x87a851a5E1852eaBA8a2124AF4FdBbc0ce73c8AB");
                            //  try {
                            // Log.d("mytest", "give " + vote.giveRightToVote(address).send());
                            //  }
                            //  catch (Exception e) {
                            //   Log.d("mytest",e.getMessage());
                            //  }

                            //  Log.d("mytest", "vote address " + vote.getContractAddress());
                            //  Log.d("mytest", "getMyVote() " + vote.getMyVote().send());
                            //  Log.d("mytest", "getTotalVoters() " + vote.getTotalVoters().send());
                            //  Log.d("mytest", "getCurrentVoters() " + vote.getCurrentVoters().send());
                            //   try {
                            //       Log.d("mytest", "vote " + vote.vote(BigInteger.valueOf(1)).send());
                            //   }
                            //   catch (Exception e){
                            //       Log.d("mytest", e.getMessage());
                            //   }
                            //   Log.d("mytest", "getMyVote() " + vote.getMyVote().send());
                            //   Log.d("mytest", "getCurrentVoters() " + vote.getCurrentVoters().send());

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
    public void createNewVote(){
        Intent myIntent = new Intent(getContext(), NewVoteActivity.class);
        myIntent.putExtra("url", connectUrl);
        getContext().startActivity(myIntent);
    }


    public void addVotes(List<VotingCard> votes){
        adapterCard.addCards(votes);
        //recyclerVotingCard.setAdapter(adapterCard);
        adapterCard.notifyDataSetChanged();
    }
}

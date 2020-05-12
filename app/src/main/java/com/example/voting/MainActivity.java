package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.contract.Vote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import jnr.ffi.annotations.In;

public class MainActivity extends AppCompatActivity {

    Button buttonDeploy;
    RecyclerView recyclerVotingCard;
    FloatingActionButton fab;
    List<VotingCard> listCard = new ArrayList<>();

    FirebaseAuth auth;

    String connectUrl = "HTTP://192.168.0.112:7545";
    //String connectUrl = "https://ropsten.infura.io/v3/6217c9661e8143cdad94007434e30c43";
    Web3j web3j;
    Vote vote;
    private final static String PRIVATE_KEY = "243f16f0f8e5ba62faa8405324087d50a70e5a7f96081f8b5cfe585baf9984b3";
    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    /*public Web3j connect(String url){
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
    }*/

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
        myIntent.putExtra("url", connectUrl);
        MainActivity.this.startActivity(myIntent);
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
                Toast.makeText(MainActivity.this, "SHO", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, StartActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBouncyCastle();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonDeploy = (Button) findViewById(R.id.buttonDeploy);
        fab = findViewById(R.id.create_button);

        //connect(connectUrl);
        web3j = VoteApplication.getInstance().getWeb3j();

        VotingCard vc = new VotingCard();
        listCard.add(vc);
       /* listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);
        listCard.add(vc);*/


        //DatabaseReference myRef = VoteApplication.getInstance().myRef;
       // DatabaseReference myRef = VoteApplication.getInstance().database;
      //  Query myQuery = myRef;


        recyclerVotingCard = findViewById(R.id.list);
        VotingCardAdapter adapterCard = new VotingCardAdapter(listCard);
        recyclerVotingCard.setAdapter(adapterCard);

/*
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                SmartContract contract = dataSnapshot.getValue(SmartContract.class);
                VotingCard vc = new VotingCard(contract.getAddress(), contract.getName(), contract.getDescription());

                adapterCard.addCard(vc);
                recyclerVotingCard.setAdapter(adapterCard);







                //adapterCard.addCard(vc);

                //Item item = dataSnapshot.getValue(Item.class);
               // VotingCard vc2 = new VotingCard(item.name, item.desc, item.address);
               // listCard.add(vc2);
                //recyclerVotingCard.setAdapter()

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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


*/

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
}

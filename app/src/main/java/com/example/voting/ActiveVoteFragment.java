package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.voting.contract.Vote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class ActiveVoteFragment extends Fragment implements UserInfoListener{

    Button buttonDeploy;
    RecyclerView recyclerVotingCard;
    FloatingActionButton fab;
    VotingCardAdapter adapterCard;

    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    String connectUrl = VoteApplication.getInstance().connectUrl;


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

        progressbar = view.findViewById(R.id.progressBarActiveVote);
        constraintLayout = view.findViewById(R.id.activeVoteActivityContent);

        buttonDeploy = (Button) view.findViewById(R.id.buttonDeploy);
        fab = view.findViewById(R.id.create_button);

        recyclerVotingCard = view.findViewById(R.id.list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerVotingCard.setLayoutManager(mLayoutManager);
        if(adapterCard==null) {
            adapterCard = new VotingCardAdapter(new ArrayList<>());
        }
        recyclerVotingCard.setAdapter(adapterCard);


        buttonDeploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            /////////////////////
                           /* String walletPassword = "secr3t";
                            String walletDirectory = "/app/wallet";

                            String walletName = WalletUtils.generateNewWalletFile(walletPassword, new File(walletDirectory));
                            //System.out.println("wallet location: " + walletDirectory + "/" + walletName);


                            Credentials credentials = WalletUtils.loadCredentials(walletPassword, walletDirectory + "/" + walletName);

                            vote = deploy(credentials,web3j);*/

////////////////////////////////////////


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

                        } catch (Exception e) {
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
                createNewVote();
            }
        });
        VoteApplication.getInstance().setListener(this);
        VoteApplication.getInstance().getUserFromFB();
    }

    public void createNewVote() {
        Intent myIntent = new Intent(getContext(), NewVoteActivity.class);
        myIntent.putExtra("url", connectUrl);
        getContext().startActivity(myIntent);
    }


    public void addVotes(List<VotingCard> votes) {
        if(adapterCard==null) {
            adapterCard = new VotingCardAdapter(new ArrayList<>());
        }
        adapterCard.addCards(votes);
        adapterCard.notifyDataSetChanged();
    }

    private void checkSecretary() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (VoteApplication.getInstance().user.isSecretary()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    fab.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    fab.setVisibility(View.GONE);
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


    @Override
    public void onInfoLoaded(User user) {
        checkSecretary();
    }
}



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

import android.os.SystemClock;
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

    RecyclerView recyclerVotingCard;
    FloatingActionButton fab;
    VotingCardAdapter adapterCard;

    ProgressBar progressbar;
    ConstraintLayout constraintLayout;

    private long mLastClickTime = 0;

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

        fab = view.findViewById(R.id.create_button);

        recyclerVotingCard = view.findViewById(R.id.list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerVotingCard.setLayoutManager(mLayoutManager);
        if(adapterCard==null) {
            adapterCard = new VotingCardAdapter(new ArrayList<>());
        }
        recyclerVotingCard.setAdapter(adapterCard);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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

    public void updateAdapterCard(List<VotingCard> votes){
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



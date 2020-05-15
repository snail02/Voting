package com.example.voting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HistoryVoteFragment extends Fragment {

    VotingCardAdapter adapterCard;
    RecyclerView recyclerVotingCard;
    String connectUrl = VoteApplication.getInstance().connectUrl;

    public HistoryVoteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryVoteFragment newInstance() {
        HistoryVoteFragment fragment = new HistoryVoteFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_history_vote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerVotingCard = view.findViewById(R.id.listHistory);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerVotingCard.setLayoutManager(mLayoutManager);
        if(adapterCard==null) {
            adapterCard = new VotingCardAdapter(new ArrayList<>());
        }
        recyclerVotingCard.setAdapter(adapterCard);
    }

    public void addVotes(List<VotingCard> votes) {
        if(adapterCard==null) {
            adapterCard = new VotingCardAdapter(new ArrayList<>());
        }
        adapterCard.addCards(votes);
        adapterCard.notifyDataSetChanged();
    }
}

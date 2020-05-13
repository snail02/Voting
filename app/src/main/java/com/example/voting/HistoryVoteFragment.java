package com.example.voting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HistoryVoteFragment extends Fragment {



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


}

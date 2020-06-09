package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {



    HistoryAction historyAction;
    Profile profile;
    ActiveVoteFragment activeVoteFragment;
    HistoryVoteFragment historyVoteFragment;

    public MainFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
        historyAction = HistoryAction.newInstance();
        profile= Profile.newInstance();
        activeVoteFragment = ActiveVoteFragment.newInstance();
        historyVoteFragment= HistoryVoteFragment.newInstance();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return  historyAction;
        }
        else if(position==1){
            return activeVoteFragment;
        }
        else if(position==2){
            return historyVoteFragment;
        }
        else{
            return profile;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    public void activeVotes(List<VotingCard> votes){
        activeVoteFragment.addVotes(votes);
    }

    public void activeVotesRemove(VotingCard voteCard){
        activeVoteFragment.deleteVotes(voteCard);
    }

    public void historyVotes(List<VotingCard> votes){
        historyVoteFragment.addVotes(votes);
    }

}

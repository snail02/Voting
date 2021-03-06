package com.example.voting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmetPagerAdapter extends FragmentPagerAdapter {

    ActiveVoteFragment activeVoteFragment;
    HistoryVoteFragment historyVoteFragment;

    public MyFragmetPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        activeVoteFragment = ActiveVoteFragment.newInstance();
        historyVoteFragment= HistoryVoteFragment.newInstance();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return  activeVoteFragment;
        }
        else {
            return historyVoteFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Активные";
        }
        else{
            return "Завершенные";
        }
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

    public void updateActiveVotes(){
        //activeVoteFragment.updateAdapterCard();
    }
}

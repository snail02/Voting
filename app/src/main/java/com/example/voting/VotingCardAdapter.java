package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class VotingCardAdapter extends RecyclerView.Adapter<VotingCardAdapter.VotingCardViewHolder> {

    private List<VotingCard> card;
    VotingCardAdapter(){

    }

    VotingCardAdapter(List<VotingCard> card){
        this.card = card;
    }

    VotingCardAdapter(VotingCard card){
        this.card.add(card);
    }

    private void openVoteActivity(Context context, int position){

        Intent myIntent = new Intent(context, VoteActivity.class);
        myIntent.putExtra("card",card.get(position));
        context.startActivity(myIntent);
    }

    @NonNull
    @Override
    public VotingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
        return new VotingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotingCardViewHolder holder, int position) {
        VotingCard votingCard = card.get(position);
        holder.nameView.setText(votingCard.getName());
        holder.descView.setText(votingCard.getDescription());
        holder.activeView.setText(votingCard.getStatusActive());
        holder.votedView.setText(votingCard.getStatusVoted());

        holder.setCardVoteClickListener(new CardVoteClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                openVoteActivity(view.getContext(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return card.size();
    }

    public class VotingCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameView, descView, activeView, votedView;
        private CardVoteClickListener cardVoteClickListener;

        public VotingCardViewHolder(@NonNull View itemView){
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.name);
            descView = (TextView) itemView.findViewById(R.id.description);
            activeView = (TextView) itemView.findViewById(R.id.active);
            votedView = (TextView) itemView.findViewById(R.id.voted);

            itemView.setOnClickListener(this);
        }

        public void setCardVoteClickListener(CardVoteClickListener cardVoteClickListener){
            this.cardVoteClickListener = cardVoteClickListener;
        }

        @Override
        public void onClick(View view) {
            cardVoteClickListener.onClick(view,getAdapterPosition());
        }
    }

    public void addCard(VotingCard card) {
        this.card.add(card);
    }

    public void addCards(List<VotingCard> list){
        card.addAll(list);
    }
}

package com.example.voting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VotingCardAdapter extends RecyclerView.Adapter<VotingCardAdapter.VotingCardViewHolder> {

    private LayoutInflater inflater;
    private List<VotingCard> card;

    VotingCardAdapter(Context context, List<VotingCard> card){
        this.card = card;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VotingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_card, parent, false);
        return new VotingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotingCardViewHolder holder, int position) {
        VotingCard votingCard = card.get(position);
        holder.nameView.setText(votingCard.getName());
        holder.descView.setText(votingCard.getDescription());
        holder.activeView.setText(votingCard.getStatusActive());
        holder.votedView.setText(votingCard.getStatusVoted());
    }

    @Override
    public int getItemCount() {
        return card.size();
    }

    public class VotingCardViewHolder extends RecyclerView.ViewHolder{

        TextView nameView, descView, activeView, votedView;

        public VotingCardViewHolder(@NonNull View itemView){
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.name);
            descView = (TextView) itemView.findViewById(R.id.description);
            activeView = (TextView) itemView.findViewById(R.id.active);
            votedView = (TextView) itemView.findViewById(R.id.voted);
        }
    }
}

package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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
    private long mLastClickTime = 0;
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

        holder.setCardVoteClickListener(new CardVoteClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                openVoteActivity(view.getContext(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return card.size();
    }

    private Integer searchById(String id){
        for (int i = 0; i < getItemCount(); i++){
            if(card.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

    public void removeById(String id) {
        int position = searchById(id);
        if(position == -1)
            return;
        card.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, card.size());
    }

    public class VotingCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameView, descView;
        private CardVoteClickListener cardVoteClickListener;

        public VotingCardViewHolder(@NonNull View itemView){
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.name);
            descView = (TextView) itemView.findViewById(R.id.description);

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
        int tmp;
        for(int i=0; i<list.size();i++){
            tmp =searchVotingCard(list.get(i));
            if(tmp!=-1){
                card.set(tmp,list.get(i));
            }
            else{
                card.add(0,list.get(i));
            }
        }

    }


    public int searchVotingCard(VotingCard dCard){
        for(int i=0;i<card.size(); i++){
            if(card.get(i).getAddress().equals(dCard.getAddress())){
                return i;
            }
        }
        return -1;
    }

    public void delCard(VotingCard dCard){
        int tmp = searchVotingCard(dCard);
        if(tmp!=-1)
        card.remove(tmp);
    }
}

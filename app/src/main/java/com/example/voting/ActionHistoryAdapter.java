package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActionHistoryAdapter extends RecyclerView.Adapter<ActionHistoryAdapter.ActionHistoryViewHolder> {

    private ArrayList<ActiveItem> activeItem;

    public ActionHistoryAdapter() {
    }

    public ActionHistoryAdapter(ArrayList<ActiveItem> activeItem) {
        this.activeItem = activeItem;
    }

    @Override
    public ActionHistoryAdapter.ActionHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item, parent, false);
        return new ActionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActionHistoryAdapter.ActionHistoryViewHolder holder, int position) {
        ActiveItem activeItems = activeItem.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate= formatter.format(activeItems.getDate());
        holder.dateView.setText(strDate);
        holder.actionView.setText(activeItems.getActive());
    }

    @Override
    public int getItemCount() {
        return activeItem.size();
    }

    public class ActionHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView dateView, actionView;

        public ActionHistoryViewHolder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.textViewDate);
            actionView = (TextView) view.findViewById(R.id.textViewAction);
        }
    }

    public void addActionItems(List<ActiveItem> list) {
        activeItem.clear();
        activeItem.addAll(list);


    }

}

package com.example.voting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryAction extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ActionHistoryAdapter adapterActionHistory;

    private OnFragmentInteractionListener mListener;

    public HistoryAction() {
        // Required empty public constructor
    }


    public static HistoryAction newInstance() {
        HistoryAction fragment = new HistoryAction();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewActionHistory);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // создаем адаптер

        // устанавливаем для списка адаптер

        if(adapterActionHistory==null) {
            adapterActionHistory = new ActionHistoryAdapter (new ArrayList<>());
        }
        recyclerView.setAdapter(adapterActionHistory);


        FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                .getHttpsCallable("getActiveUser")
                .call().addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
            @Override
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                Log.d("mytest",httpsCallableResult.getData().toString());
                HashMap<String, ArrayList<Object>> map = (HashMap<String, ArrayList<Object>>) httpsCallableResult.getData();
                ArrayList<Object> list = map.get("active");
                ArrayList<ActiveItem> list2 = new ArrayList<>() ;
                for(int i=0; i<list.size(); i++){

                    long sec = ((HashMap<String,Integer>)((HashMap<String, Object>)list.get(i)).get("date")).get("_seconds") ;
                    long milisec = sec*1000;
                    Date date = new Date(milisec + 5*3600*1000);

                    String active = ((HashMap<String,String>)list.get(i)).get("active");
                    ActiveItem activeItem = new ActiveItem(date, active);
                    list2.add(activeItem);

                }

                adapterActionHistory.addActionItems(list2);




            }
        });



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history_action, container, false);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void addItems(List<ActiveItem> items) {
        if(adapterActionHistory==null) {
            adapterActionHistory = new ActionHistoryAdapter(new ArrayList<>());
        }
        adapterActionHistory.addActionItems(items);
    }
}

package com.example.voting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Profile extends Fragment implements UserInfoListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

TextView surename;
TextView name;
TextView otv;

    private OnFragmentInteractionListener mListener;

    public Profile() {

    }



    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        surename = view.findViewById(R.id.surname);
        name = view.findViewById(R.id.name);
        otv = view.findViewById(R.id.otv);

        VoteApplication.getInstance().setListener(this);
        VoteApplication.getInstance().getUserFromFB();
        getUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
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

    public void getUserInfo(){
        surename.setText(VoteApplication.getInstance().user.getFam());
        name.setText(VoteApplication.getInstance().user.getName());
        otv.setText(VoteApplication.getInstance().user.getPat());
    }

    @Override
    public void onInfoLoaded(User user) {
        getUserInfo();
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

package com.example.voting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class Profile extends Fragment implements UserInfoListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

TextView surename;
TextView name;
TextView otv;
Button buttonSignOut;

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
        buttonSignOut = view.findViewById(R.id.buttonSignOut);

        VoteApplication.getInstance().setListener(this);
        VoteApplication.getInstance().getUserFromFB();
        getUserInfo();

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        VoteApplication.getInstance().users.child(VoteApplication.getInstance().auth.getCurrentUser().getUid()).child("fcmtoken").setValue("");

        FirebaseAuth.getInstance().signOut();
        //finish();
        startActivity(new Intent(getContext(), StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

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

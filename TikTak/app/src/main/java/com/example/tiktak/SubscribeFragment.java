package com.example.tiktak;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribeFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Intent i = getActivity().getIntent();

        Client c = (Client) i.getSerializableExtra("Client");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscribe, container, false);


    }
}
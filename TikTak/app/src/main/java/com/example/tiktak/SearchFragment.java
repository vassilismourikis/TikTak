package com.example.tiktak;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    Client c;
    ArrayList<String> arrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_search, container, false);

        Intent i = getActivity().getIntent();

       c = (Client) i.getSerializableExtra("Client");
        //TODO:GET AVAILABLE CHANNELS
        c.getConsumer().a=true;
        arrayList= c.getConsumer().getAvailableChannelsArray();
        System.out.println(arrayList);

        // Inflate the layout for this fragment
        final ListView list = view.findViewById(R.id.list);






        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(SearchFragment.this.getContext(),clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), UsersVideos.class);

                startActivity(intent);
            }
        }
    );



        final Button refresh = (Button)view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment fragment = new SearchFragment();
                getFragmentManager().beginTransaction().replace(R.id.searchfrag, fragment).commit();
            }
        });





        return view;
    }


}
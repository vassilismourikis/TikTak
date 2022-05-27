package com.example.tiktak;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.tiktak.MainActivity.c;

public class ProfileFragment extends Fragment {
    Client c;
    ArrayList<String> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        Intent i = getActivity().getIntent();

        c = (Client) i.getSerializableExtra("Client");
        //TODO:GET AVAILABLE CHANNELS
        try {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    arrayList= c.getPublisher().getChannelsVideos(c.getPublisher().toString());
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(arrayList +"PROFFFFFFFFFFFFFFFFRAG");

        // Inflate the layout for this fragment
        final ListView list = view.findViewById(R.id.list);





        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(ProfileFragment.this.getContext(),clickedItem, Toast.LENGTH_SHORT).show();
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



        final Button refresh = (Button)view.findViewById(R.id.upload);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wsefa");
                //TODO: NEA ACTIVITY FOR UPLOAD
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            c.getPublisher().addHashtag("#FUNNY,#WOW,#yolo,#phososhooting,#interesting");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }
        });





        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ProfileFragment fragment = new ProfileFragment();
            getFragmentManager().beginTransaction().replace(R.id.profile_frag, fragment).commit();
        }
    }
}
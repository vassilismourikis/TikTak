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

import java.util.ArrayList;

public class SubscribeFragment extends Fragment {
    Client c;
    ArrayList<String> arrayList;
    Button mButton;
    EditText mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_subscribe, container, false);

        Intent i = getActivity().getIntent();

        c = (Client) i.getSerializableExtra("Client");
        //TODO:GET AVAILABLE CHANNELS
        arrayList= c.getConsumer().getSubs();
        System.out.println(arrayList + "SUBFRAG");

        // Inflate the layout for this fragment
        final ListView list = view.findViewById(R.id.list);




        mButton = (Button)view.findViewById(R.id.sub);

        mButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View inView) {
                mEdit = (EditText)view.findViewById(R.id.search_text2);
                String channel=mEdit.getText().toString();
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        c.getConsumer().subscribe(channel);
                        return null;
                    }
                }.execute();

            }
        });



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(SubscribeFragment.this.getContext(),clickedItem, Toast.LENGTH_SHORT).show();
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



        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SubscribeFragment fragment = new SubscribeFragment();
            getFragmentManager().beginTransaction().replace(R.id.sub_frag, fragment).commit();
        }
    }
}
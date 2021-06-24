package com.example.tiktak;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import java.util.concurrent.ExecutionException;

public class SearchFragment extends Fragment {
    Client c;
    ArrayList<String> arrayList;
    Button mButton;
    EditText mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_search, container, false);

        Intent i = getActivity().getIntent();

       c = (Client) i.getSerializableExtra("Client");
        //TODO:GET AVAILABLE CHANNELS
        try {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    arrayList= c.getConsumer().getAvailableChannelsArray();
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(arrayList);

        // Inflate the layout for this fragment
        final ListView list = view.findViewById(R.id.list);




        mButton = (Button)view.findViewById(R.id.search);

        mButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View inView) {
                mEdit = (EditText)view.findViewById(R.id.search_text);
                String hashtag=mEdit.getText().toString();
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        c.getConsumer().SelectHashtag(hashtag);
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
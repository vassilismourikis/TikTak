package com.example.tiktak;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.tiktak.MainActivity.c;


public class UsersVideos extends AppCompatActivity {


    ArrayList<String> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_users_videos);

        Bundle b = getIntent().getExtras();


        String name =  b.getString("Client");

        // Inflate the layout for this fragment
        final ListView list =findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    videos=c.getPublisher().getChannelsVideos(name);
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arrayList=videos;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(UsersVideos.this,clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {
                                            try {
                                                new AsyncTask<Void, Void, Void>() {

                                                    @Override
                                                    protected Void doInBackground(Void... voids) {
                                                        c.getConsumer().pullVideo(videos.get(position));
                                                        return null;
                                                    }
                                                }.execute().get();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }



                                            Intent intent = new Intent(UsersVideos.this, VideoPlayer.class);

                                            startActivity(intent);
                                        }
                                    }
        );

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }


}
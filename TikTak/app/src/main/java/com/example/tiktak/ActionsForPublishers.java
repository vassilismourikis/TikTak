package com.example.tiktak;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ActionsForPublishers extends AsyncTask<String, String, String> {
    ObjectInputStream in;
    ObjectOutputStream out;
    SocketAddress publishersPort;
    Publisher p;
    byte[] bytee=null;
    public ActionsForPublishers(Socket connection, Publisher p) {
        //ο constructor αρχικοποιεί τα αντικείμενα-ροές για την επικοινωνία με τον αντίστοιχο πελάτη
        try {
            //System.out.println("AC FOR PUB");
            out = new ObjectOutputStream(connection.getOutputStream());
            //out: για γράψιμο στον πελάτη

            in = new ObjectInputStream(connection.getInputStream());
            //in: για διάβασμα από τον πελάτη
            this.publishersPort=connection.getRemoteSocketAddress();
            this.p=p;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {


        try {

            Message abMessage = (Message)in.readObject();

            String a = abMessage.getA(); //videokey
            int b = Integer.parseInt(abMessage.getB1());

            if(b<=p.getChunkSize(a)-1) {
                bytee=p.getChunk(a,b);
                out.writeObject(bytee);
                out.flush();
                //System.out.println(b +" IN AC FOR PUB CHUNKS");
            }
            else{
                out.writeObject(null);
                out.flush();
            }






        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally
        {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    return null;
    }
}
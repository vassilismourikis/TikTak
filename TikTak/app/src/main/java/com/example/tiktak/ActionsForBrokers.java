package com.example.tiktak;

import android.os.AsyncTask;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ActionsForBrokers extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    BrokerNode broker;
    Socket connection;
    public ActionsForBrokers(Socket connection, BrokerNode broker) {
        //ο constructor αρχικοποιεί τα αντικείμενα-ροές για την επικοινωνία με τον αντίστοιχο πελάτη
        try {
            this.connection=connection;
            out = new ObjectOutputStream(connection.getOutputStream());
            //out: για γράψιμο στον πελάτη

            in = new ObjectInputStream(connection.getInputStream());
            //in: για διάβασμα από τον πελάτη
            this.broker=broker;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


        try {
            //System.out.println("INTO ACTIONS FOR BROKERS ");

            Message abMessage = (Message)in.readObject();


                if (abMessage.getC() == 1)
                    broker.saveHashtag(abMessage);
                else if (abMessage.getC() == 2)
                    broker.saveChannelName(abMessage);
                else if (abMessage.getC() == 5) {
                    out.writeObject(broker.getRelatedVideos(abMessage.getA(),abMessage.getB()));
                    out.flush();
                }
                else if(abMessage.getC() == 10){//UPDATE BROKERS LIST
                    broker.updateBrokersData(abMessage.con,abMessage.getA());
                }

        }  catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally
         {
            try {
                in.close();
                out.close();
                connection.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
}

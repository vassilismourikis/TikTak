package com.example.tiktak;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class ActionsForConsumers extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    SocketAddress consumersPort;
    Consumer c;
    byte[] bytee=null;
    public ActionsForConsumers(Socket connection, Consumer c) {
        //ο constructor αρχικοποιεί τα αντικείμενα-ροές για την επικοινωνία με τον αντίστοιχο πελάτη
        try {
            //System.out.println("AC FOR PUB");
            out = new ObjectOutputStream(connection.getOutputStream());
            //out: για γράψιμο στον πελάτη

            in = new ObjectInputStream(connection.getInputStream());
            //in: για διάβασμα από τον πελάτη
            this.consumersPort=connection.getRemoteSocketAddress();
            this.c=c;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


        try {

            Message abMessage = (Message)in.readObject();



            if(abMessage.getC()==15) {
                if(!abMessage.getB().equals(c.getConsumerName())) c.print(abMessage.getA()); //the statement for not to notify the publisher for his own video
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

    }
}


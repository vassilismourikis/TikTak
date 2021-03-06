package com.example.tiktak;


import android.os.AsyncTask;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class PublisherThread extends Thread {


    private Socket connection;
    private ServerSocket providerSocket;
    private Publisher p;

    public PublisherThread(Publisher p){
        connection=null;
        providerSocket=p.getServer();
        //System.out.println("PUBLISHERS IP:  "+p.getServer().getInetAddress().toString().substring(1));
        this.p=p;
    }

    @Override
    public void run(){
        while(true){
            try {
                //System.out.println("publisher thread");
                //waiting for client
                connection = providerSocket.accept();

                ActionsForPublishers t1 = new ActionsForPublishers(connection,p);


                t1.start();
            }catch(SocketException e){
                //System.out.println("publisherThread terminated");
                break;
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

}

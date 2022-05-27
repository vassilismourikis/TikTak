package com.example.tiktak;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Refresher extends Thread {
    private Container data;
    private BrokerNode broker;
    private int port1,port2;
    ObjectOutputStream out1,out2 = null;
    private ArrayList<String> networks;
    private String ip1,ip2;

    public Refresher(Container d,BrokerNode b){
        networks=new ArrayList<String>();
        try {
            File myObj = new File("broker.txt");
            Scanner myReader = new Scanner(myObj);
            int counter=0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(counter==0 || counter==3 || counter==6){
                    networks.add(data);
                }
                counter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        this.broker=b;
        this.data=d;
        //System.out.println(broker.getSocket().getLocalPort());
        if(broker.getBrokerSocket().getLocalPort()==64284){
            port1=64285;
            port2=64286;
            ip1=networks.get(1);
            ip2=networks.get(2);
        }
        else if(broker.getBrokerSocket().getLocalPort()==64285){
            port1=64284;
            port2=64286;
            ip1=networks.get(0);
            ip2=networks.get(2);
        }
        else {
            port1=64284;
            port2=64285;
            ip1=networks.get(0);
            ip2=networks.get(1);
        }
    }
    @Override
    public void run(){
        Socket s1=null,s2=null;
        try {
            s1 = new Socket(ip1,port1);
            s2 = new Socket(ip2,port2);
            out1 = new ObjectOutputStream(s1.getOutputStream());
            out2 = new ObjectOutputStream(s2.getOutputStream());


            out1.writeObject(new Message(null,data,String.valueOf(broker.port),null,null,null,null,10));//stelnei ta nea dedomena
            out1.flush();
            out2.writeObject(new Message(null,data,String.valueOf(broker.port),null,null,null,null,10));//stelnei ta nea dedomena
            out2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            try{
//                s1.close();
//                s2.close();
//                out1.close();
//                out2.close();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//
//        }

    }

}

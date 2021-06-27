package com.example.tiktak;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerThread extends Thread {
    private Socket connection;
    private ServerSocket providerSocket;
    private BrokerNode broker;

    public BrokerThread(BrokerNode broker){
        connection=null;
        providerSocket=null;
        this.broker=broker ;
    }

    @Override
    public void run() {
        while(true){
            try {
                //waiting for client
                connection = broker.getBrokerSocket().accept();

                ActionsForBrokers t1 = new ActionsForBrokers(connection,broker);

                t1.start();

            }
            catch (EOFException e){
                System.out.println(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

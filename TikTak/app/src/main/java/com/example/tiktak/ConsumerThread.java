package com.example.tiktak;


import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ConsumerThread extends Thread{

        private Socket connection;
        private ServerSocket providerSocket;
        private Consumer c;

        public ConsumerThread(Consumer c){
            connection=null;
            providerSocket=c.getServer();
            this.c=c;
        }

        @Override
        public void run() {
            while(true){
                try {
                    //System.out.println("publisher thread");
                    //waiting for client
                    connection = providerSocket.accept();

                    ActionsForConsumers t1 = new ActionsForConsumers(connection,c);


                    t1.execute();
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


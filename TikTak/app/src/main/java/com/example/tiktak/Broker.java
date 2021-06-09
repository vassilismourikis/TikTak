package com.example.tiktak;



public interface Broker extends Node {


    public void calculateKeys();


    public Client acceptConnection(Client p);

    /*
    make it client because all  clients on tik tok are both cons and publ

    public Publisher acceptConnection(Publisher p );


    public Consumer acceptConnection (Consumer c );

     */

    public void notifyPublisher(String s);

    public void notifyBrokersOnChanges();

    public byte[] pull(String videokey,int u);

    public void filterConsumers();

}

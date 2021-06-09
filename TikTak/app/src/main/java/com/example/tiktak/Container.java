package com.example.tiktak;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

public class Container implements Serializable {

    private TreeSet<String> hashtags;
    private TreeSet<String> channelNames;
    private int port;

    public Container(TreeSet<String> h,TreeSet<String> c,int p){
        this.channelNames=c;
        this.hashtags=h;
        this.port=p;
    }

    public TreeSet<String> getHashtags(){
        return this.hashtags;
    }

    public TreeSet<String> getChannelNames(){
        return this.channelNames;
    }


}

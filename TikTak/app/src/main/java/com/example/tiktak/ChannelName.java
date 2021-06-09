package com.example.tiktak;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelName {
    private String channelName;
    private ArrayList<String> hashtagsPublished;
    private HashMap<String, ArrayList<Value>> userVideoFilesMap;


    public ChannelName(String s){
        this.channelName=s;
    }
    @Override
    public String toString(){
        return channelName;
    }
}

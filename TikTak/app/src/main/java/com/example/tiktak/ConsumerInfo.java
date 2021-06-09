package com.example.tiktak;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ConsumerInfo implements Serializable{

        String channelName;
        private ArrayList<String> subs;
        private String address;
        private int port;

        public ConsumerInfo(String name, ArrayList<String> v, String s,int port){
            this.channelName=name;
            this.subs=v;
            this.address=s;
            this.port=port;
        }

        public String getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public ArrayList<String> getSubs() {
            return subs;
        }

        public void setVideos(ArrayList<String> subs) {
            this.subs = subs;
        }
    }


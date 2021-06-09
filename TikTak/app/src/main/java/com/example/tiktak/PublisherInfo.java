package com.example.tiktak;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Map;

public class PublisherInfo implements Serializable {
    String channelName;
    private Map<String, Value> videos;
    private String address;
    private int port;

    public PublisherInfo(String name, Map<String, Value> v, String s, int port){
        this.channelName=name;
        this.videos=v;
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

    public Map<String, Value> getVideos() {
        return videos;
    }

    public void setVideos(Map<String, Value> videos) {
        this.videos = videos;
    }
}

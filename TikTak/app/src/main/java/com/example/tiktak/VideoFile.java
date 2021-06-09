package com.example.tiktak;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoFile implements Serializable {


    private String videokey;
    private String videoName;
    private String channelName;
    private String dateCreated;
    private String length;
    private String framerate;
    private String frameWidth;
    private String frameHeight;
    private List<String> associatedHashtags;
    private ArrayList<byte[]> chunks;

    public VideoFile(String videokey, String videoName, String channelName, String dateCreated, String length, String framerate, String frameWidth, String frameHeight, ArrayList<String> h) {
        this.videokey = videokey;
        this.videoName = videoName;
        this.channelName = channelName;
        this.dateCreated = dateCreated;
        this.length = length;
        this.framerate = framerate;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.associatedHashtags=h;
    }

    public int getChunksSize(){
        return chunks.size();
    }
    public byte[] getChunk(int i){
        return chunks.get(i);
    }

    public void addChunks(byte[] bts) {
        int blockSize = 512 * 1024;
        ArrayList<byte[]> list = new ArrayList<>();
        //System.out.println(bts.length % blockSize);
        int blockCount = (bts.length + blockSize - 1) / blockSize;
        byte[] range = null;

        for (int i = 1; i < blockCount; i++) {
            int idx = (i - 1) * blockSize;
            range = Arrays.copyOfRange(bts, idx, idx + blockSize);
            list.add(range);
        }
        int end = -1;
        if (bts.length % blockSize == 0) {
            end = bts.length;
        } else {
            end = bts.length % blockSize + blockSize * (blockCount - 1);
        }

        range = Arrays.copyOfRange(bts, (blockCount - 1) * blockSize, end);
        list.add(range);

        chunks=list;
        //System.out.println(chunks.size() +" Size of chunk list");
    }

    public String getVideokey(){
        return videokey;
    }
}

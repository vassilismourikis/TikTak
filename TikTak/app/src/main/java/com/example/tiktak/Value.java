package com.example.tiktak;

import java.io.Serializable;

public class Value implements Serializable {
    private VideoFile video;

    public Value(VideoFile v){
        this.video=v;
    }

    @Override
    public String toString() {
        return "Value{" +
                "video=" + video +
                '}';
    }

    public VideoFile getVideo(){
        return video;
    }
}

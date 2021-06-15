package com.example.tiktak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    VideoView videoView;
    MediaController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView=(VideoView) findViewById(R.id.videoView);

        mController= new MediaController(this);

        videoView.setVideoPath("");

        mController.setAnchorView(videoView);

        videoView.setMediaController(mController);

        videoView.start();
    }
}
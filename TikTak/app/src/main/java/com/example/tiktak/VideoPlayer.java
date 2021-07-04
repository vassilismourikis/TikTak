package com.example.tiktak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import static com.example.tiktak.MainActivity.c;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

public class VideoPlayer extends AppCompatActivity {

    VideoView videoView;
    MediaController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView=(VideoView) findViewById(R.id.videoView);

        mController= new MediaController(this);

        File dir = getExternalStorageDirectory();
        String path = dir.getAbsolutePath()+"/Android/media/";
            videoView.setVideoPath(path+"Client"+c.getPublisher().toString()+"/Consumer/"+"output.mp4");

        mController.setAnchorView(videoView);

        videoView.setMediaController(mController);

        videoView.start();
    }
}
package com.example.tiktak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class VideoRecorder extends AppCompatActivity {

    private static int VIDEO_REQUEST=101;
    private Uri videoUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recorder);
    }


    public void captureVideo(View view){
        Intent videoInent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(videoInent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(videoInent,VIDEO_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
    }
}
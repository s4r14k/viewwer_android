package com.priscilla.viewwer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.priscilla.viewwer.R;

public class VideoTutoActivity extends AppCompatActivity {

    private VideoView VideoTutoriel;
    private MediaController mediaController;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tuto);

        VideoTutoriel = (VideoView) findViewById(R.id.VideoTutoriel);

        mediaController = new MediaController(this);

        Bundle tutorial = getIntent().getExtras();

        if(tutorial.getString("tutorial").equals("Tutoriel de Prise de Vue")){
            String url = "https://icreadev.mg/videos/tuto_prise.mp4";
            uri = Uri.parse(url);
        }else if(tutorial.getString("tutorial").equals("Tutoriel Hotspots")){
            String url = "https://icreadev.mg/videos/tuto_hotspot.mp4";
            uri = Uri.parse(url);
        }
//        else{
//            Intent intent = new Intent(this, CarousselActivity.class);
//            startActivity(intent);
//            finish();
//        }


        VideoTutoriel.setVideoURI(uri);

        mediaController.setAnchorView(VideoTutoriel);

        VideoTutoriel.setMediaController(mediaController);

        VideoTutoriel.start();



    }

}

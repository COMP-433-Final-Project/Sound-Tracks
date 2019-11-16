package com.example.soundtracks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SoundTrackMenu extends AppCompatActivity {

    private Button home;
    private Button newPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_track_menu);
        home = findViewById(R.id.home);
        newPlaylist = findViewById(R.id.addnew);
    }

    public void onClick(View view){
        if(view == home){
            finish();
        }
        else if(view == newPlaylist){
            Intent intent = new Intent(this, CreateSoundTrack.class);
            startActivity(intent);
        }

    }
}

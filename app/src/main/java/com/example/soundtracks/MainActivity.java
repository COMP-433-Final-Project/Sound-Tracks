package com.example.soundtracks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button soundTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundTracks = findViewById(R.id.soundTracks);
    }


    public void onClick(View view){
        if(view == soundTracks){
            Intent intent = new Intent(this, SoundTrackMenu.class);
            intent.putExtra(Intent.EXTRA_TEXT, "text from an intent");
            startActivity(intent);
        }
    }
}

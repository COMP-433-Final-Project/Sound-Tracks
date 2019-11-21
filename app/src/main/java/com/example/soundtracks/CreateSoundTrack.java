package com.example.soundtracks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateSoundTrack extends AppCompatActivity {

    private Button mCancel;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sound_track);

        mCancel = findViewById(R.id.cancel);
    }

    public void onClick(View view){
        if(view == mCancel){
            finish();
        }
    }

    //checking to see if added to my branch
}

package com.example.soundtracks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateSoundTrack extends AppCompatActivity {
    private DbHelper mDatabase;
    private EditText mName;
    private Button mCancel;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sound_track);
        mCancel = findViewById(R.id.cancel);
        mSubmit = findViewById(R.id.submit);
        mName = findViewById(R.id.name_field);

        mDatabase = new DbHelper(this);

    }

    public void onClick(View view){
        if(view == mCancel){
            finish();
        }
        else if(view == mSubmit){
            new Saver(getText(mName), "70", "20", "5").execute();
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            String table = mDatabase.tableToString(db, DataBaseContract.PlayListTable.TABLE_NAME);
            MainActivity.log((table));

        }
    }

    private static String getText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null || editable.length() == 0) {
            return null;
        }
        return editable.toString();
    }

    private class Saver extends AsyncTask<Void, Void, Boolean> {
        private final String mName;
        private final String mLatitude;
        private final String mLongitude;
        private final String mRadius;

        public Saver(String name, String latitude, String longtiude, String radius) {
            mName = name;
            mLatitude = latitude;
            mLongitude = longtiude;
            mRadius = radius;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(CreateSoundTrack.this, mName + " saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(CreateSoundTrack.this, "Failed to insert/update " + mName,
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return mDatabase.insertPlaylist(mName, mLatitude, mLongitude, mRadius);
        }
    }





}

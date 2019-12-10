package com.example.soundtracks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SoundTrackMenu extends AppCompatActivity {

    private Button home;
    private Button newPlaylist;
    private DbHelper mDatabase;
    ArrayList<String> arrayList;
    private ListView list;
    private Button mDelete;
    private boolean mSelected = false;
    private int mPosition;
    private String mPlayListSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_track_menu);

        home = findViewById(R.id.home);
        newPlaylist = findViewById(R.id.addnew);
        list = findViewById(R.id.list);
        mDatabase = new DbHelper(this);
        arrayList  = new ArrayList<>();
        mDelete = findViewById(R.id.delete);
        getNames();
    }

    public void onClick(View view){
        if(view == home){
            finish();
        }
        else if(view == newPlaylist){
            Intent intent = new Intent(this, CreateSoundTrack.class);
            startActivity(intent);
        }
        else if(view == mDelete){
            MainActivity.log("I am here");
            SQLiteDatabase dbr = mDatabase.getReadableDatabase();
            String table = mDatabase.tableToString(dbr, DataBaseContract.PlayListTable.TABLE_NAME);
            MainActivity.log((table));
            deleteRow(mPlayListSelected);

            Toast.makeText(SoundTrackMenu.this,"Delete successful",Toast.LENGTH_SHORT).show();

        }

    }

    public void deleteRow(String nameToDelete){
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        MainActivity.log("DELETE FROM " + DataBaseContract.PlayListTable.TABLE_NAME
                + " WHERE " + DataBaseContract.PlayListTable.COLUMN_NAME_NAME + " = " + '"' + nameToDelete + '"');
        db.execSQL("DELETE FROM " + DataBaseContract.PlayListTable.TABLE_NAME
               + " WHERE " + DataBaseContract.PlayListTable.COLUMN_NAME_NAME + " = " + '"' + nameToDelete + '"');

        db.close();

    }
    public void getNames(){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DataBaseContract.PlayListTable.TABLE_NAME,null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String column1 = c.getString(1);
                // Do something Here with values

                arrayList.add(column1);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                if(!mSelected){
                    list.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    mSelected = true;
                    mPosition = position;
                    mPlayListSelected = clickedItem;

                }else if(mSelected && mPosition == position){
                    list.getChildAt(position).setBackgroundColor(Color.WHITE);
                    mSelected = false;
                    mPosition = 0;
                }
                //Toast.makeText(SoundTrackMenu.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
    }
}

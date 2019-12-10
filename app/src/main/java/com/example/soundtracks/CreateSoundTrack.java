package com.example.soundtracks;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CreateSoundTrack extends AppCompatActivity {
    private DbHelper mDatabase;
    private EditText mName;
    private Button mCancel;
    private Button mSubmit;
    private Button mCurrentLocation;
    private Button mAddLocation;
    private String mLatitude;
    private String mLongitude;
    private TextView mTextViewLatitude;
    private TextView mTextViewLongitude;
    private ArrayList<String> mArrayList;
    private ListView list;
    private boolean mSelected = false;
    private int mPosition;
    private String mSongSelected;

    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    public static List<Geofence> geofenceList = new ArrayList<Geofence>();

    private FusedLocationProviderClient fusedLocationClient;


    private static final int GEOFENCE_RADIUS_IN_METERS = 15;
    private static final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sound_track);
        mCancel = findViewById(R.id.cancel);
        mSubmit = findViewById(R.id.submit);
        mName = findViewById(R.id.name_field);
        mCurrentLocation = findViewById(R.id.currentLocation);
        mAddLocation = findViewById(R.id.addLocation);
        mTextViewLatitude = findViewById(R.id.latitude);
        mTextViewLongitude = findViewById(R.id.longitude);
        mArrayList  = new ArrayList<>();
        list = findViewById(R.id.list);

        geofencingClient = LocationServices.getGeofencingClient(this);
        mDatabase = new DbHelper(this);

        getNames();

    }

    public void onClick(View view){
        if(view == mCancel){
            finish();
        }
        else if(view == mSubmit){
            if(mName.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter a Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if(mLatitude.isEmpty() || mLongitude.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select a Location", Toast.LENGTH_SHORT).show();
            }


            // TODO: Check through database to see if new location is within geofence radius of any existing geofences
//            for(Geofence geofence : geofenceList){
//                if(Location.distanceBetween(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude),
//                geofence.)
//            }

            //if(mLatitude.)
            new Saver(getText(mName), mLatitude, mLongitude, "5").execute();
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            String table = mDatabase.tableToString(db, DataBaseContract.PlayListTable.TABLE_NAME);
            MainActivity.log((table));
            MainActivity.log(Long.toString(mDatabase.lastInsertID));


            geofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(mSongSelected)

                    .setCircularRegion(
                            Double.parseDouble(mLatitude),
                            Double.parseDouble(mLongitude),
                            GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences added
                            // ...
                            MainActivity.log("added geofence");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to add geofences
                            // ...
                        }
                    });

        }
        else if (view == mAddLocation){
            Intent mapIntent =  new Intent(getApplicationContext(), SetLocation.class);
            startActivityForResult(mapIntent, LOCATION_REQUEST_CODE);
        }
        else if (view == mCurrentLocation){
            mLatitude = Double.toString(MainActivity.mCurrentLatitude);
            mLongitude = Double.toString(MainActivity.mCurrentLongitude);
            mTextViewLatitude.setText("Latitude: " + mLatitude);
            mTextViewLongitude.setText("Longitude: " + mLongitude);
        }
    }
    public void getNames(){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DataBaseContract.SongTable.TABLE_NAME,null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String column1 = c.getString(1);
                // Do something Here with values

                mArrayList.add(column1);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);

                if(!mSelected){
                    list.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    mSelected = true;
                    mPosition = position;
                    mSongSelected = clickedItem;
                }else if(mSelected && mPosition == position){
                    list.getChildAt(position).setBackgroundColor(Color.WHITE);
                    mSelected = false;
                    mPosition = 0;

                }
               // Toast.makeText(CreateSoundTrack.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mLatitude = data.getStringExtra("latitude");
                mLongitude = data.getStringExtra("longitude");
                Toast.makeText(getApplicationContext(), mLatitude + " " + mLongitude, Toast.LENGTH_SHORT).show();
                mTextViewLatitude.setText("Latitude: " + mLatitude.substring(0, 9));
                mTextViewLongitude.setText("Longitude: " + mLongitude.substring(0, 9));
            }
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

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private void setmLatLng(String aLat, String aLong) {
        mLatitude = aLat;
        mLongitude = aLong;
    }


}

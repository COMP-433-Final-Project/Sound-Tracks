package com.example.soundtracks;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {


    public static void log(String msg) {
        Log.d("THOMAS", msg);
    }
    public static void makeToast(String msg, Context c){Toast.makeText(c, msg,Toast.LENGTH_SHORT).show();}
    public static double mCurrentLatitude;
    public static double mCurrentLongitude;
    Button soundTracks;
    Button mPlay;
    Button mPause;
    private LocationManager mLocationManager;
    private static final int REQUEST_CODE = 73;
    private static String mPath;
    private static MediaPlayer mp;
    private DbHelper mDatabase;
    private boolean mFirstTime = true;
    ArrayList<String> mArrayList;
    private static Resources r;
    private static TextView mCurrentSong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundTracks = findViewById(R.id.soundTracks);
        mPlay = findViewById(R.id.play);
        mPause = findViewById(R.id.pause);
        mCurrentSong = findViewById(R.id.currentSong);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        r = getResources();

        mDatabase = new DbHelper(this);
        mArrayList = new ArrayList<>();



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            accessLocation();
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE);
            }else{
                Toast.makeText(this, "Check permission settings", Toast.LENGTH_SHORT);
            }
        }


    }


    public void onClick(View view){

        if(view == soundTracks){
            Intent intent = new Intent(this, SoundTrackMenu.class);
            intent.putExtra(Intent.EXTRA_TEXT, "text from an intent");
            startActivity(intent);

        }
        else if(view == mPlay){
            mp.start();
        }
        else if(view == mPause){
            if(mp.isPlaying()) {
                mp.pause();
            } else {
                mp.start();
            }
        }
    }

    public static void playSong(Context context, String name){
        log(name);
        mCurrentSong.setText(name);
        int musicId = r.getIdentifier(name, "raw", "com.example.soundtracks");
        mp = MediaPlayer.create(context, musicId);
        mp.start();

    }

    public static void stopSong(){
        mp.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessLocation();
            } else {
                Toast.makeText(this, "Did not get permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void accessLocation() {
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    10000, 10, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 10, this);
        } catch (SecurityException e) {
            Toast.makeText(this, "Could not get updates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        Toast.makeText(this,"Latitude: " + Double.toString(mCurrentLatitude) + "Long: " + Double.toString(mCurrentLongitude),
                Toast.LENGTH_SHORT);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}



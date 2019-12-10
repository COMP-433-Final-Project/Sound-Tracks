package com.example.soundtracks;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {


    public static void log(String msg) {
        Log.d("THOMAS", msg);
    }
    public static double mCurrentLatitude;
    public static double mCurrentLongitude;
    Button soundTracks;
    private LocationManager mLocationManager;
    private static final int REQUEST_CODE = 73;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundTracks = findViewById(R.id.soundTracks);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
        log("lat: " + Double.toString(mCurrentLatitude));
        log("long: " + Double.toString(mCurrentLongitude));

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

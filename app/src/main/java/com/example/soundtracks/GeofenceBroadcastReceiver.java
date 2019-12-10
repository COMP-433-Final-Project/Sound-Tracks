package com.example.soundtracks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;
import java.util.MissingFormatArgumentException;

import static android.content.ContentValues.TAG;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    Context context;
    // ...
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        context = context;
        MainActivity.log("broadcast received");
        if (geofencingEvent.hasError()) {
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());
            String errorMessage = "geofencing event has error";
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String playListName = triggeringGeofences.get(0).getRequestId();
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){


                MainActivity.log(playListName + " entered");
                MainActivity.makeToast(playListName + " entered", context);
                MainActivity.playSong(context, playListName);
            }

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){

                MainActivity.log(triggeringGeofences.get(0).getRequestId() + " exited");
                MainActivity.makeToast(playListName + " exited", context);
                MainActivity.stopSong();
            }


            // Get the transition details as a String.
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(
//                    this,
//                    geofenceTransition,
//                    triggeringGeofences
//            );

            // Send notification and log the transition details.
            //sendNotification(geofenceTransitionDetails);
            //Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
        }
    }

}
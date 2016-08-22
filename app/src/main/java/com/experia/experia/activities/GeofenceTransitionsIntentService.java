package com.experia.experia.activities;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by Sam on 8/22/16.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    protected static final String TAG = "GeofenceTransitionsIntentService";
    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, intent.toString());
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "geogfencingevent error code = "+geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        Log.d(TAG, Integer.toString(geofenceTransition));
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            String[] geofenceIds = new String[geofencingEvent.getTriggeringGeofences().size()];
            for(int i=0; i<geofenceIds.length; ++i){
                geofenceIds[i] = geofencingEvent.getTriggeringGeofences().get(i).getRequestId();

                Log.d(TAG, "geofenceIds="+geofenceIds[i]);
            }
            // Send notification and log the transition details.
            //  sendNotification(geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "geofencing invalid type");
        }
    }
}


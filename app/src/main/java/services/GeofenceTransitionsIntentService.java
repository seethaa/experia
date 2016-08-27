package services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.experia.experia.R;
import com.experia.experia.activities.MainActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Constants;
import models.NamedGeofence;

public class GeofenceTransitionsIntentService extends IntentService {

    // region Properties

    private final String TAG = GeofenceTransitionsIntentService.class.getName();

    private SharedPreferences prefs;
    private Gson gson;

    // endregion

    // region Constructors

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    // endregion

    // region Overrides

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        prefs = getApplicationContext().getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        gson = new Gson();

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
                onError(event.getErrorCode());
            } else {
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    List<String> geofenceIds = new ArrayList<>();
                    for (Geofence geofence : event.getTriggeringGeofences()) {
                        geofenceIds.add(geofence.getRequestId());
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        onEnteredGeofences(geofenceIds);
                    }
                }
            }
        }
    }

    // endregion

    // region Private

    private void onEnteredGeofences(List<String> geofenceIds) {
        for (String geofenceId : geofenceIds) {
            String geofenceName = "";

            // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference
            Map<String, ?> keys = prefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String jsonString = prefs.getString(entry.getKey(), null);
                NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
                if (namedGeofence.id.equals(geofenceId)) {
                    geofenceName = namedGeofence.name;
                    break;
                }
            }
            Log.d("DEBUG", geofenceId);

            // send notification to user
            createBigPictureStyleNoti(72,R.drawable.ic_logo_placeholder, "Oktoberfest!",
                    "Live Band playing at the Roxy");

            // Set the notification text and send the notification
//            String contextText = String.format(this.getResources().getString(R.string.Notification_Text), geofenceName);
//
//            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent intent = new Intent(this, AllGeofencesActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(this.getResources().getString(R.string.Notification_Title))
//                    .setContentText(contextText)
//                    .setContentIntent(pendingNotificationIntent)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setAutoCancel(true)
//                    .build();
//            notificationManager.notify(0, notification);

        }
    }

    private void onError(int i) {
        Log.e(TAG, "Geofencing Error: " + i);
    }

    // endregion

    //BigPicture Style Layout
    private void createBigPictureStyleNoti (int nId, int iconRes, String title, String body) {
        // First let's define the intent to trigger when notification is selected
        // Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(this, MainActivity.class);
        // Next, let's turn this into a PendingIntent using
        //   public static PendingIntent getActivity(Context context, int requestCode,
        //       Intent intent, int flags)
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_UPDATE_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        // .setLargeIcon expects a bitmap
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_bitmap_lg_crown);

        // Big picture for style
        final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.big_oktober_fest);

        //Items visible in Collapsed InboxView
        String contentText = "Learn archery from the best instructors who'll take the time to get " +
                "you up and running and see you hitting the target.";
        String subText = "Located in SoMa";

        //Items visible in Expanded InboxView
        String bigContentText = "Learn the archery of Robin Hood";
        String summaryText = "Serious fun with bows & arrows";

        // InboxStyle Notification
        Notification bigPictStyleNotification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(contentText)
                .addAction(R.drawable.ic_phone_call,"ACTION",pIntent)
                .addAction(R.drawable.ic_calendar,"ACTION",pIntent)
                .addAction(R.drawable.ic_invite_friends,"ACTION",pIntent)
                .setSmallIcon(iconRes)  //miniature
                .setLargeIcon(largeIcon)
                .setSubText(subText)
                .setAutoCancel(true) // Hides the notification after its been selected
                .setPriority(util.Constants.NOTICE_PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(bigContentText)
                        .setSummaryText(summaryText)
                        .bigPicture(picture))
                .build();

        // Get the notification manager system service
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, bigPictStyleNotification);
    }
}


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
import com.experia.experia.activities.PostDetailActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Constants;
import models.Experience;

public class GeofenceTransitionsIntentService extends IntentService {

    // region Properties

    private final String TAG = GeofenceTransitionsIntentService.class.getName();

    private SharedPreferences prefs;
    private Gson gson;
    private DatabaseReference mDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            final String geofenceName = "";
            final String key = "-KQmjdn-AFxR3kWypKN6";

            mDatabase.child("posts").child(key).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Experience exp = dataSnapshot.getValue(Experience.class);
                            // send notification to user
                            try {
                                createBigPictureStyleNoti(72, key, R.drawable.ic_logo_placeholder, exp.title,
                                        exp.description, exp.address, exp.imgURL);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getKey:onCancelled", databaseError.toException());
                        }
                    });

//            // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference
//            Map<String, ?> keys = prefs.getAll();
//            for (Map.Entry<String, ?> entry : keys.entrySet()) {
//                String jsonString = prefs.getString(entry.getKey(), null);
//                NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
//                if (namedGeofence.id.equals(geofenceId)) {
//                    geofenceName = namedGeofence.title;
//                    break;
//                }
//            }
//            Log.d("DEBUG", geofenceId);

//            // send notification to user
//            createBigPictureStyleNoti(72,R.drawable.ic_logo_placeholder, geofenceName,
//                    "Live Band playing at the Roxy");
        }
    }

    private void onError(int i) {
        Log.e(TAG, "Geofencing Error: " + i);
    }

    // endregion

    //BigPicture Style Layout
    private void createBigPictureStyleNoti (int nId, String key, int iconRes, String title, String desc, String location, String imgURL) throws IOException {
        // First let's define the intent to trigger when notification is selected
        // Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, key);
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
//        URL url = new URL(imgURL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.connect();
//        InputStream input = connection.getInputStream();
//        Bitmap picture = BitmapFactory.decodeStream(input);
        //Items visible in Collapsed InboxView
        String contentText = "Learn archery from the best instructors who'll take the time to get " +
                "you up and running and see you hitting the target.";
        String subText = "Located in SoMa";

        //Items visible in Expanded InboxView
        String bigContentText = "Learn the archery of Robin Hood";
        String summaryText = desc.substring(0, 30) + "...";
        String atLocation = "At " + location;

        // InboxStyle Notification
        Notification bigPictStyleNotification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(atLocation)
                .addAction(R.drawable.icon_athlete,"",pIntent)
                .addAction(R.drawable.icon_heart, "",pIntent)
                .addAction(R.drawable.icon_join, "", pIntent)
                .setSmallIcon(iconRes)  //miniature
                .setLargeIcon(largeIcon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText(summaryText)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true) // Hides the notification after its been selected
                .setPriority(Notification.PRIORITY_DEFAULT)
                //.setPriority(util.Constants.NOTICE_PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(title)
                        .setSummaryText(summaryText)
                        .bigPicture(picture))
                .build();

//        NotificationManager notiManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle("Hello")
//                .setContentText("hello")
//                .setSmallIcon(R.drawable.ic_invite_friends)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setAutoCancel(false);

//        notiManager.notify(0, mBuilder.build());
//        Log.i("here", "here");


        // Get the notification manager system service
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, bigPictStyleNotification);
    }
}


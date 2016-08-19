package services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.experia.experia.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        // request token that will be used by the server to send push notifications
        String token = instanceID.getToken();
        Log.d(TAG, "FCM Registration Token: " + token);

        // pass along this data
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}

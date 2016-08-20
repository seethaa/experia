package com.experia.experia.activities;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by seetha on 8/19/16.
 */
public class Experia extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}

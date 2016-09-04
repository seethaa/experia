package com.experia.experia.activities;

import android.app.Activity;
import android.os.Bundle;

import com.experia.experia.R;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;

/**
 * Created by doc_dungeon on 9/4/16.
 */
public class UberActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_uber);


        RideRequestButton requestButton = (RideRequestButton) findViewById(R.id.uber_login_button);
        RideParameters rideParams = new RideParameters.Builder()
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco") // Price estimate will only be provided if this is provided.
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d") // Optional. If not provided, the cheapest product will be used.
                .build();

        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("YOUR_CLIENT_ID")
                .setServerToken("YOUR_SERVER_TOKEN")
                .build();
        ServerTokenSession session = new ServerTokenSession(config);

        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {

            }

            @Override
            public void onError(ApiError apiError) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        requestButton.setRideParameters(rideParams);
        requestButton.setSession(session);
        requestButton.setCallback(callback);
        requestButton.loadRideInformation();

        //This is a convenience method and will set the default config to be used in other components without passing it directly.
        UberSdk.initialize(config);
    }
}
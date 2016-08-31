package com.experia.experia.activities;

import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.experia.experia.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import fragments.BookmarksFragment;
import fragments.CreateExNameDescriptionPhotoFragment;
import fragments.LocationSettingsFragment;
import fragments.PostListFragment;
import fragments.ProfileFragment;
import models.NamedGeofence;

public class MainActivity extends BaseActivity implements CreateExNameDescriptionPhotoFragment.OnNameDescriptionPhotoCompleteListener,
                GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationSettingsFragment fmMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        //Set up tabs

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        GeofenceController.getInstance().init(this);
        //Hardcode for geofence example
        NamedGeofence geofence = new NamedGeofence();
        geofence.name = "Event1";
        geofence.latitude = 37.41069;
        geofence.longitude = -121.93855;
        geofence.radius = 20f;

        //TODO put geofence from Geofire
        GeofenceController.getInstance().addGeofence(geofence, geofenceControllerListener);
        mGoogleApiClient = GeofenceController.getInstance().googleApiClient;
        Log.d("DDB", mGoogleApiClient.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.i(MainActivity.class.getSimpleName(), "googlePlayServicesCode = " + googlePlayServicesCode);

        if (googlePlayServicesCode == 1 || googlePlayServicesCode == 2 || googlePlayServicesCode == 3) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCode, this, 0).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onMapClick(MenuItem mi) {

        //change this to maps
//        startActivity(new Intent(Ma.this, NewPostActivity.class));

        // handle click here
    }




    // region GeofenceControllerListener

    private GeofenceController.GeofenceControllerListener geofenceControllerListener = new GeofenceController.GeofenceControllerListener() {
        @Override
        public void onGeofencesUpdated() {
            //TODO update the geoFire
        }

        @Override
        public void onError() {
            Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onNameDescriptionPhotoCompleted(String title, String description, String imgURL) {
        //do nothing for now..
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Display the connection status
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            //Toast.makeText(getContext(), "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            if(fmMap.isAdded()) {
                fmMap.map.moveCamera(cameraUpdate);
                fmMap.startLocationUpdates();
            }

        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    // endregion

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] { "Nearby", "Search", "Favorites", "Profile" };
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0){//Nearby
                Fragment cndpf = PostListFragment.newInstance(null);
                return cndpf;
            }
            else if (position ==1){//Search
                fmMap = LocationSettingsFragment.newInstance(null);
                return fmMap;
            }
//            else if (position ==2){//Create
//                Fragment cndpf = .newInstance(null);
//                return cndpf;
//            }
            else if (position ==2){//Favorite
                BookmarksFragment bmf = BookmarksFragment.newInstance(null);
                return bmf;
            }
            else if (position ==3){//Profile
                ProfileFragment pf = ProfileFragment.newInstance(null);
                return pf;
            }
            else{
                Fragment cndpf = PostListFragment.newInstance(null);
                return cndpf;
            }
        }



        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
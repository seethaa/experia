package com.experia.experia.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.experia.experia.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

import fragments.BookmarksFragment;
import fragments.CreateExNameDescriptionPhotoFragment;
import fragments.LocationSettingsFragment;
import fragments.PostListFragment;
import fragments.ProfileFragment;
import fragments.RecentPostsFragment;
import models.NamedGeofence;

public class MainActivity extends BaseActivity implements CreateExNameDescriptionPhotoFragment.OnNameDescriptionPhotoCompleteListener {

    //custom notification
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    NotificationCompat.Builder mBuilder;

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


//        mNavDrawer.getMenu().getItem(0).setChecked(true);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, new RecentPostsFragment()).commit();
//        setTitle("Experiences around me");

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, NewPostActivity.class));
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });

        GeofenceController.getInstance().init(this);
        //Hardcode for geofence example
        NamedGeofence geofence = new NamedGeofence();
        geofence.name = "Event1";
        geofence.latitude = 37.41069;
        geofence.longitude = -121.93855;
        geofence.radius = 20f;

        //TODO put geofence from Geofire
        GeofenceController.getInstance().addGeofence(geofence, geofenceControllerListener);
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
    public void onNameDescriptionPhotoCompleted(String title, String description, Uri imgUri) {
        //do nothing for now
    }

    // endregion

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;
        private String tabTitles[] = new String[] { "Nearby", "Search", "Create", "Favorites", "Profile" };
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
//                    RecentPostsFragment rpf = PostListFragment.newInstance(null);
//                    return rpf;
                    PostListFragment cndpf = RecentPostsFragment.newInstance(null);
                    return cndpf;
                }
                else if (position ==1){//Search
                    LocationSettingsFragment cndpf = LocationSettingsFragment.newInstance(null);
                    return cndpf;
                }
                else if (position ==2){//Search
                    LocationSettingsFragment cndpf = LocationSettingsFragment.newInstance(null);
                    return cndpf;
                }
                else if (position ==3){//Search
                    BookmarksFragment bmf = BookmarksFragment.newInstance(null);
                    return bmf;
                }
                else if (position ==4){//Search
                    ProfileFragment pf = ProfileFragment.newInstance(null);
                    return pf;
                }
                else{
                    return null;
                }
            }



        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
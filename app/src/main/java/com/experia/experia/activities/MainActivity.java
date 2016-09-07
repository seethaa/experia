package com.experia.experia.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.experia.experia.R;
import com.firebase.geofire.GeoLocation;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fragments.BookmarksFragment;
import fragments.CreateExNameDescriptionPhotoFragment;
import fragments.LocationSettingsFragment;
import fragments.PostListFragment;
import fragments.ProfileFragment;
import fragments.RecentPostsFragment;
import models.Experience;
import models.User;

public class MainActivity extends BaseActivity implements CreateExNameDescriptionPhotoFragment.OnNameDescriptionPhotoCompleteListener,
                GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
LocationSettingsFragment.OnMapCameraChangeListener{

    public ArrayList<Experience> experiences;
    public GoogleApiClient mGoogleApiClient;
//    public GeofenceController geofenceController;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationSettingsFragment fmMap;
    private DatabaseReference mDatabase;
    private String TAG = "MainActivity";
    public int filterType = 31; //5b'11111
    User userClicked;
    String mDisplayName;
    private MaterialSimpleListAdapter materialDialogAdapter;
    FABToolbarLayout toolbarLayout;
    LinearLayout toolbar;

    android.support.design.widget.FloatingActionButton fab;
    boolean fabbarOpen = false;

    @BindView(R.id.ivfabfun) ImageView fabFun;
    @BindView(R.id.ivfabadventure) ImageView fabAdventure;
    @BindView(R.id.ivfabimpact) ImageView fabImpact;
    @BindView(R.id.ivfabrelax) ImageView fabRelax;
    @BindView(R.id.ivfablearn) ImageView fabLearn;

    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        unbinder = ButterKnife.bind(this);



        experiences = new ArrayList<Experience>();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabtoolbar_fab);
        toolbarLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);

        toolbar = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .build();
        mGoogleApiClient.connect();
        //set up actionbar TODO
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Set up tabs

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));
        viewPager.setOffscreenPageLimit(5);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

//
//        geofenceController = GeofenceController.getInstance();
//        geofenceController.init(this);
//
//
//        NamedGeofence geofence = new NamedGeofence();
//        geofence.title = "test";
//        geofence.desc = "desc";
//        geofence.latitude = 37.403561;
//        geofence.longitude = -121.95787069;
//        geofence.radius = 0.3f;
//
//
//        //TODO put geofence from Geofire
//        geofenceController.addGeofence(geofence, geofenceControllerListener);
//


        //Hardcode for geofence example

        mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                final Experience experience = snapshot.getValue(Experience.class);
                DatabaseReference geoFireLocation = mDatabase.child("path/to/geofire").child(snapshot.getKey());
                Log.d(TAG, "geoFireLocation = " + geoFireLocation.toString());
                if (geoFireLocation != null) {
                    geoFireLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            double latitude = (double) snapshot.child("l/0").getValue();
                            double longitude = (double) snapshot.child("l/1").getValue();

//                            Log.d(TAG, "experience = " + experience.toString());
//                            Log.d(TAG, "latitude = " + Double.toString(latitude) + ", longitude = " + Double.toString(longitude));
//                            NamedGeofence geofence = new NamedGeofence();
//                            geofence.title = experience.title;
//                            geofence.desc = experience.description;
//                            geofence.latitude = latitude;
//                            geofence.longitude = longitude;
//                            geofence.radius = 20f;
//
//                            //TODO put geofence from Geofire
//                            //geofenceController.addGeofence(geofence, geofenceControllerListener);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                materialDialogAdapter = new MaterialSimpleListAdapter(getApplicationContext());
                materialDialogAdapter.addAll(createDataArrayList());


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });


        getCurrentUserInfo();
//        mGoogleApiClient = GeofenceController.getInstance().googleApiClient;
//        Log.d("DDB", mGoogleApiClient.toString());

        //USing for testing right now...
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout toolbar = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);
                toolbarLayout.show();
                fabbarOpen = true;

                //startActivity(new Intent(MainActivity.this, NewPostActivity.class));

//                Intent i = new Intent(MainActivity.this, NewPostActivity.class);
//                i.putExtra("displayName", mDisplayName);
//                startActivity(i);
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_filter).setVisible(false);
        return true;
    }

    @OnClick(R.id.ivfabfun)
    public void funClick(View view) {
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        i.putExtra("displayName", mDisplayName);
        i.putExtra("category", 5);
        startActivity(i);
    }

    @OnClick(R.id.ivfabadventure)
    public void adventureClick(View view) {
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        i.putExtra("displayName", mDisplayName);
        i.putExtra("category", 1);
        startActivity(i);
    }

    @OnClick(R.id.ivfabimpact)
    public void impactClick(View view) {
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        i.putExtra("displayName", mDisplayName);
        i.putExtra("category", 3);
        startActivity(i);
    }

    @OnClick(R.id.ivfabrelax)
    public void relaxClick(View view) {
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        i.putExtra("displayName", mDisplayName);
        i.putExtra("category", 2);
        startActivity(i);
    }

    @OnClick(R.id.ivfablearn)
    public void learnClick(View view) {
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        i.putExtra("displayName", mDisplayName);
        i.putExtra("category", 4);
        startActivity(i);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_filter:
                    //Toast.makeText(this, "Filter selected", Toast.LENGTH_SHORT)
                    //        .show();
                    showMaterialDialog();
                    break;
                case android.R.id.home:
                    Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
    }

    @Override
    public void onBackPressed() {
        if (fabbarOpen) {

            toolbarLayout.hide();
        }
        else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        fmMap.onActivityResult(requestCode, resultCode, data);
    }


//    // region GeofenceControllerListener
//
//    private GeofenceController.GeofenceControllerListener geofenceControllerListener = new GeofenceController.GeofenceControllerListener() {
//        @Override
//        public void onGeofencesUpdated() {
//        }
//
//        @Override
//        public void onError() {
//            Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//    };


    @Override
    public void onNameDescriptionPhotoCompleted(String title, String description, String imgURL, String mTags) {
        //do nothing for now..
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


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

    @Override
    public void onMapCameraChange(DataSnapshot snapshot, HashMap<String, GeoLocation> geoKeyMap) {
        experiences.clear();
        for (DataSnapshot Snapshot: snapshot.getChildren()) {
            String key = Snapshot.getKey();
            if(geoKeyMap.containsKey(key)) {
                Experience exp = Snapshot.getValue(Experience.class);
                Log.d(TAG, "type = " + exp.type+ ", filterType = "+filterType);
                if(exp.type==0 || (1 << (exp.type - 1) & filterType) != 0) {
                    experiences.add(exp);
                    Log.d(TAG, "key in geoKeySet: " + key);
                }
            }
            else
                Log.d(TAG, "key not in geoKeySet: " + key);

        }
        fmMap.updateAdapter(experiences);
    }

    public void getCurrentUserInfo() {

            Query userQuery = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(getUid());


            userQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userClicked = dataSnapshot.getValue(User.class);

                    System.out.println("DEBUGGY USER CLICKED " + userClicked.getDisplayName() + " " +userClicked.getEmail());

                    mDisplayName = userClicked.getDisplayName();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    // endregion

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] { "Nearby", "Search", "Favorites", "Profile"};
        private Context context;

        private int[] imageResId = {
                R.drawable.tab_icon_home,
                R.drawable.tab_icon_map,
                R.drawable.tab_icon_favorites,
                R.drawable.tab_icon_profile
        };

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
                PostListFragment cndpf = RecentPostsFragment.newInstance(null);
                return cndpf;
            }
            else if (position ==1){//Search
                fmMap = LocationSettingsFragment.newInstance(null);
                return fmMap;
            }

            else if (position ==2){//Create
                PostListFragment bmf = BookmarksFragment.newInstance(null);
                return bmf;
            }
            else if (position ==3){//Favorite
                Fragment bmf = BookmarksFragment.newInstance(null);
                return bmf;
            }
            else if (position ==4){//Profile
                ProfileFragment pf = ProfileFragment.newInstance(null);
                return pf;
            }
//
            else{
                PostListFragment cndpf = RecentPostsFragment.newInstance(null);
                return cndpf;
            }
        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // Generate title based on item position
//            return tabTitles[position];
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            // return tabTitles[position];

            // getDrawable(int i) is deprecated, use getDrawable(int i, Theme theme) for min SDK >=21
            // or ContextCompat.getDrawable(Context context, int id) if you want support for older versions.
            // Drawable image = context.getResources().getDrawable(iconIds[position], context.getTheme());
            // Drawable image = context.getResources().getDrawable(imageResId[position]);

            Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;

//            // Generate title based on item position
//            Drawable image = context.getResources().getDrawable(imageResId[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            // Replace blank spaces with image icon
//            SpannableString sb = new SpannableString("   " + tabTitles[position]);
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
        }


    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Log.d(TAG, String.valueOf(mGoogleApiClient.isConnected()));
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                //Toast.makeText(getContext(), "GPS location was found!", Toast.LENGTH_SHORT).show();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                if(fmMap.isAdded()) {
                    if(fmMap.map != null) {
                        fmMap.map.moveCamera(cameraUpdate);
                        fmMap.startLocationUpdates(mGoogleApiClient);
                    }
                }

            } else {
                Toast.makeText(getApplicationContext(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e(TAG, "Connecting to GoogleApiClient suspended.");
        }
    };


    private void showMaterialDialog() {

        int count = 0;
        int tmp = 1;
        for(int i=0; i<5; ++i){
            if((tmp & filterType) != 0) {
                count++;
            }
            tmp <<= 1;
        }
        Integer[] iArray = new Integer[count];
        int k=0;
        for(int i=0; i<5; ++i){
            if(((1 << i) & filterType )!=0){
                iArray[k++] = i;
            }
        }

        new MaterialDialog.Builder(this)
                .title("Select types")
                .items(createDataArrayList())
                .itemsCallbackMultiChoice(iArray, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        filterType = 0;
                        for(Integer w:which){
                            filterType += (1 << w);
                        }
                        fmMap.resetFirebaseValueEvent();
                        return true; // allow selection
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.selectAllIndicies();
                    }
                })
                //.alwaysCallMultiChoiceCallback()
                .positiveText("Apply")
                .negativeText(R.string.cancel)
                .autoDismiss(true)
                .neutralText("Apply All")
                .show();
    }

    private ArrayList<MaterialSimpleListItem> createDataArrayList() {
        ArrayList<MaterialSimpleListItem> dataArrayList = new ArrayList<>();
        dataArrayList.add(new MaterialSimpleListItem.Builder(this)
                .content("Adventure")
                .icon(R.drawable.icon_map_adventure)
                .build());
        dataArrayList.add(new MaterialSimpleListItem.Builder(this)
                .content("Relax")
                .icon(R.drawable.icon_map_relax)
                .build());
        dataArrayList.add(new MaterialSimpleListItem.Builder(this)
                .content("Social")
                .icon(R.drawable.icon_map_social)
                .build());
        dataArrayList.add(new MaterialSimpleListItem.Builder(this)
                .content("Learn")
                .icon(R.drawable.icon_map_learn)
                .build());
        dataArrayList.add(new MaterialSimpleListItem.Builder(this)
                .content("Fun")
                .icon(R.drawable.icon_map_fun)
                .build());
        
        return dataArrayList;
    }



}
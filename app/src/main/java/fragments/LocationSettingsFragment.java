package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.experia.experia.Manifest;
import com.experia.experia.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import adapters.MapFragmentAdapter;
import models.Experience;
import models.User;
import permissions.dispatcher.NeedsPermission;
import services.MapPermissionsDispatcher;


public class LocationSettingsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener ,
        GoogleMap.OnCameraIdleListener{

    private static final String TAG = "MapMarkerFragment";
    private GoogleMap map;
    private MapView mMapView;
    private ImageButton mZoomInButton;
    private ImageButton mZoomOutButton;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private DatabaseReference ref;
    private GeoFire geoFire;
    private int screenLength = 1080;
    private LinearLayoutManager mManager;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private RecyclerView mRecycler;
    private MapFragmentAdapter mAdapter;
    private DatabaseReference mDatabase;
    private ArrayList<Experience> experiences;
    private HashSet<String> geoKeySet;
    private ValueEventListener valueEventListener;

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */


    public static LocationSettingsFragment newInstance(User currUser) {
        LocationSettingsFragment fg = new LocationSettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("currUser", Parcels.wrap(currUser));
        fg.setArguments(args);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        geoFire = new GeoFire(ref);
        experiences = new ArrayList<Experience>();
        geoKeySet = new HashSet<String>();
        mAdapter = new MapFragmentAdapter(getContext(), experiences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mZoomInButton = (ImageButton) rootView.findViewById(R.id.btnZoomIn);
        mZoomOutButton = (ImageButton) rootView.findViewById(R.id.btnZoomOut);
        mZoomInButton.setOnClickListener(this);
        mZoomOutButton.setOnClickListener(this);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.map_experience_list);
        mRecycler.setHasFixedSize(true);


        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mMapView != null) {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        }
        else{
            Toast.makeText(getContext(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                experiences.clear();
                for (DataSnapshot Snapshot: snapshot.getChildren()) {
                    String key = Snapshot.getKey();
                    if(geoKeySet.contains(key)) {
                        Experience exp = Snapshot.getValue(Experience.class);
                        exp.key = key;
                        experiences.add(exp);
                        Log.d(TAG,"key in geoKeySet: " + key);
                    }
                    else
                        Log.d(TAG, "key not in geoKeySet: " + key);

                }
                //TODO clean mAdapter
                mAdapter = new MapFragmentAdapter(getContext(), experiences);
                mRecycler.setAdapter(mAdapter);
                Log.d(TAG,"experience called onece");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("exp ", "The read failed: " + firebaseError.toString());
            }
        };
        mDatabase.child("posts").addValueEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        connectClient();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.d("DEBUG", "onPause");
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            //mGoogleApiClient.disconnect();
            Log.d("DEBUG", mGoogleApiClient.toString());
            Log.d("DEBUG", "onStop");
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(experiences != null){
            experiences.clear();
        }
        Log.d("DEBUG", "onDestroy");
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(getContext(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            MapPermissionsDispatcher.getMyLocationWithCheck(this);
        } else {
            Toast.makeText(getContext(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
        map.setOnCameraIdleListener(this);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenLength = Math.min(size.x, size.y);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getMyLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            map.setMyLocationEnabled(true);

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            connectClient();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }






    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        Log.d("DEBUG", mGoogleApiClient.toString());
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getActivity().getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }


    @Override
    public void onCameraIdle() {
        float zoom = map.getCameraPosition().zoom;
        double latitude = map.getCameraPosition().target.latitude;
        double longitude = map.getCameraPosition().target.longitude;
        Log.d(TAG, "zoom = " + Float.toString(zoom) + ", lat = " + Double.toString(latitude) + ", log = " + Double.toString(longitude));
        double radius = calculateZoomLevel(zoom)/2000;
        if(radius < 300)
            queryGeoFire(radius, latitude, longitude);
    }

    private void queryGeoFire(double radius, double latitude, double longitude) {
        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);
        geoKeySet.clear();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                addMarker(key, location);
                geoKeySet.add(key);
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
                resetFirebaseValueEventListener();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }

    private void resetFirebaseValueEventListener() {
        Log.d(TAG, "resetFirebaseValueEventListener");
        mDatabase.child("posts").removeEventListener(valueEventListener);
        //TODO clean valueEventListener
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                experiences.clear();
                for (DataSnapshot Snapshot: snapshot.getChildren()) {
                    String key = Snapshot.getKey();
                    if(geoKeySet.contains(key)) {
                        Experience exp = Snapshot.getValue(Experience.class);
                        exp.key = key;
                        experiences.add(exp);
                        Log.d(TAG,"key in geoKeySet: " + key);
                    }
                    else
                        Log.d(TAG, "key not in geoKeySet: " + key);

                }
                //TODO clean mAdapter
                mAdapter = new MapFragmentAdapter(getContext(), experiences);
                mRecycler.setAdapter(mAdapter);
                Log.d(TAG,"experience called onece");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("exp ", "The read failed: " + firebaseError.toString());
            }
        };
        mDatabase.child("posts").addValueEventListener(valueEventListener);
    }

    private double calculateZoomLevel(float zoom) {
        double equatorLength = 40075004; // in meters
        double widthInPixels = screenLength;
        double metersPerPixel = equatorLength / 1024;
        double diameter = 0.0;
        float zoomLevel = (float) 1.0;
        while (zoomLevel < zoom) {
            metersPerPixel /= 2;
            zoomLevel = (float) (zoomLevel + 1.0);
        }
        diameter = metersPerPixel * widthInPixels;
        Log.i(TAG, "diameter = "+diameter);
        return diameter;
    }




    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Display the connection status
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            map.moveCamera(cameraUpdate);

        } else {
            Toast.makeText(getContext(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getContext(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getContext(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(TAG, msg);
        //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
                connectionResult.startResolutionForResult(getActivity(),
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
            Toast.makeText(getContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    private void addMarker(final String key, final GeoLocation location) {
        mDatabase.child("posts").child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Experience exp = dataSnapshot.getValue(Experience.class);
                        Log.d("DEBUG", exp.toString());
                        // [START_EXCLUDE]
                        if (exp == null) {
                            // User is null, error out
                            Log.e(TAG, "Experience " + key + " is unexpectedly null");
                        } else {
                            int type = exp.type;
                            System.out.println("DEBUG GOT HERE");
                            Log.d(TAG, Integer.toString(type));
                            setMarker(type, location);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }

    private void setMarker(int type, GeoLocation location) {
        System.out.println("DEBUG SET Marker type = "+ type);
        // Set the color of the marker to green
        BitmapDescriptor defaultMarker;
        switch(type){
            case 1:
                defaultMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_favorite);
                break;
            case 2:
                defaultMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_invite_friends);
                break;
            case 3:
                defaultMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_calendar);
                break;
            default:
                defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
        }

        Log.d(TAG, defaultMarker.toString());


        // listingPosition is a LatLng point
        LatLng listingPosition = new LatLng(location.latitude, location.longitude);
        // Create the marker on the fragment
        Log.d("DEBUG", map.toString());

        Marker mapMarker = map.addMarker(new MarkerOptions()
                .position(listingPosition)
                .title("title1")
                .snippet("desc1")
                .icon(defaultMarker));

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnZoomIn) {
            map.moveCamera(CameraUpdateFactory.zoomIn());
        }
        else if (i == R.id.btnZoomOut){
            map.moveCamera(CameraUpdateFactory.zoomOut());
        }


    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference){
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}

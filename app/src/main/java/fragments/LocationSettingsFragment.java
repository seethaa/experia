package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import adapters.MapFragmentAdapter;
import adapters.PostViewHolder;
import models.Experience;
import models.User;
import permissions.dispatcher.NeedsPermission;
import services.MapPermissionsDispatcher;


public class LocationSettingsFragment extends Fragment implements
        LocationListener, View.OnClickListener ,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapMarkerFragment";
    public GoogleMap map;
    private MapView mMapView;
    private ImageButton mZoomInButton;
    private ImageButton mZoomOutButton;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private DatabaseReference ref;
    private GeoFire geoFire;
    private int screenLength = 1080;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;
    private MapFragmentAdapter mAdapter;
    private DatabaseReference mDatabase;
    private HashMap<String, GeoLocation> geoKeyMap;
    private ValueEventListener valueEventListener;
    private OnMapCameraChangeListener listener;
    private ArrayList<Marker> markerList;
    PlaceAutocompleteFragment autocompleteFragment;

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
        geoKeyMap = new HashMap<String, GeoLocation>();
        markerList = new ArrayList<Marker>();

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

        if (mMapView != null) {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(getContext(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
        if (autocompleteFragment == null) {


            autocompleteFragment = (PlaceAutocompleteFragment)
                    getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i(TAG, "Place: " + place.getAddress());
                    Log.i(TAG, "location: " + place.getLatLng().toString());
                    if (place.getLatLng() != null) {
                        //Toast.makeText(getContext(), "GPS location was found!", Toast.LENGTH_SHORT).show();
                        LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        map.animateCamera(cameraUpdate);

                        BitmapDescriptor markerId =
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        map.addMarker(new MarkerOptions()
                                .position(place.getLatLng())
                                .title(place.getName().toString())
                                .snippet(place.getAddress().toString())
                                .icon(markerId));

                    }
                }

                @Override
                public void onError(Status status) {
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listener.onMapCameraChange(snapshot, geoKeyMap);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("exp ", "The read failed: " + firebaseError.toString());
            }
        };
        mDatabase.child("posts").addValueEventListener(valueEventListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        autocompleteFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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




    private void queryGeoFire(double radius, double latitude, double longitude) {
        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);
        if(geoKeyMap != null && !geoKeyMap.isEmpty()) geoKeyMap.clear();
        Log.d(TAG, geoQuery.toString());
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                geoKeyMap.put(key, location);
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
                resetFirebaseValueEvent();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }



    private double calculateZoomLevel(float zoom) {
        double equatorLength = 40075004; // in meters
        double widthInPixels = screenLength;
        double metersPerPixel = equatorLength / 2048;
        double diameter = 0.0;
        float zoomLevel = (float) 1.0;
        while (zoomLevel < zoom) {
            metersPerPixel /= 2;
            zoomLevel = (float) (zoomLevel + 1.0);
        }
        diameter = metersPerPixel * widthInPixels;
        Log.i(TAG, "diameter = " + diameter);
        return diameter;
    }

public void startLocationUpdates(GoogleApiClient googleApiClient) {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                mLocationRequest, this);
    }



    private void resetFirebaseValueEvent() {
        Log.d(TAG, "resetFirebaseValueEvent");
        mDatabase.child("posts").removeEventListener(valueEventListener);
        //TODO clean valueEventListener
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listener.onMapCameraChange(snapshot, geoKeyMap);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("exp ", "The read failed: " + firebaseError.toString());
            }
        };
        mDatabase.child("posts").addValueEventListener(valueEventListener);
    }

    public void updateAdapter(ArrayList<Experience> experiences) {
        mAdapter = new MapFragmentAdapter(getContext(), experiences);
        mRecycler.setAdapter(mAdapter);
        setMarker(experiences);
        Log.d(TAG, "experience called onece");
    }

    // Listener start //

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(TAG, msg);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnZoomIn) {
            map.moveCamera(CameraUpdateFactory.zoomIn());
        } else if (i == R.id.btnZoomOut) {
            map.moveCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onCameraIdle() {
        float zoom = map.getCameraPosition().zoom;
        double latitude = map.getCameraPosition().target.latitude;
        double longitude = map.getCameraPosition().target.longitude;
        Log.d(TAG, "zoom = " + Float.toString(zoom) + ", lat = " + Double.toString(latitude) + ", log = " + Double.toString(longitude));
        double radius = calculateZoomLevel(zoom) / 2000;
        if (radius < 2000)
            queryGeoFire(radius, latitude, longitude);
    }

    // Listener end //


    // Interface start //

    // Define the events that the fragment will use to communicate
    public interface OnMapCameraChangeListener {
        // This can be any number of events to be sent to the activity
        void onMapCameraChange(DataSnapshot snapshot, HashMap<String, GeoLocation> geoKeyMap);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapCameraChangeListener) {
            listener = (OnMapCameraChangeListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    // Interface end //




    // Marker functions start //
    private void setMarker(ArrayList<Experience> experiences) {
        markerList.clear();
        for (Experience exp : experiences) {
            System.out.println("DEBUG SET Marker type = " + exp.type);
            // Set the color of the marker to green
            BitmapDescriptor markerId = BitmapDescriptorFactory.fromResource(PostViewHolder.chooseIcon(exp.type));



            // listingPosition is a LatLng point
            LatLng listingPosition = new LatLng(geoKeyMap.get(exp.postId).latitude, geoKeyMap.get(exp.postId).longitude);
            // Create the marker on the fragment
            Log.d("DEBUG", map.toString());

            Marker mapMarker = map.addMarker(new MarkerOptions()
                    .position(listingPosition)
                    .title(exp.title)
                    .snippet(exp.addressName)
                    .icon(markerId));
            markerList.add(mapMarker);
        }
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < markerList.size(); ++i) {
            Marker m = markerList.get(i);
            if (m.equals(marker))
                mRecycler.smoothScrollToPosition(i);
        }
        marker.showInfoWindow();
        return true;
    }
    // Marker function end


}

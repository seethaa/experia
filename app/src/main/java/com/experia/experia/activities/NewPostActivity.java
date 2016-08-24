package com.experia.experia.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.experia.experia.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import models.Experience;
import models.User;

public class NewPostActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference geoRef;
    private GeoFire geoFire;
    // [END declare_database_ref]

    private EditText mTitleField;
    private EditText mBodyField;
    private EditText mNumberGuests;
    private EditText mDate;
    private EditText mDuration;
    private EditText mTags;
    private EditText mImgURL;
    private EditText mAddress;
    private EditText mLatitude;
    private EditText mLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mTitleField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);

        mNumberGuests = (EditText) findViewById(R.id.field_numGuests);
        mDate = (EditText) findViewById(R.id.field_date);
        mDuration = (EditText) findViewById(R.id.field_duration);
        mTags = (EditText) findViewById(R.id.field_tags);
        mImgURL = (EditText) findViewById(R.id.field_imgURL);
        mAddress = (EditText) findViewById(R.id.field_address);
        mLatitude = (EditText) findViewById(R.id.field_latitude);
        mLongitude = (EditText) findViewById(R.id.field_longitude);


        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        geoRef = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        geoFire = new GeoFire(geoRef);
    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final String numGuests = mNumberGuests.getText().toString();
        final String date = mDate.getText().toString();
        final String duration = mDuration.getText().toString();
        final String tags = mTags.getText().toString();
        final String imgURL = mImgURL.getText().toString();
        final String address = mAddress.getText().toString();
        double latitude;
        double longitude;
        try{
            latitude = Double.parseDouble(mLatitude.getText().toString());
        } catch (final NumberFormatException e) {
            latitude = 1.0;
        }
        try{
            longitude = Double.parseDouble(mLongitude.getText().toString());
        } catch (final NumberFormatException e) {
            longitude = 1.0;
        }

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(numGuests)) {
            mNumberGuests.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(duration)) {
            mDuration.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(tags)) {
            mTags.setError(REQUIRED);
            return;
        }

        // [START single_value_read]
        final String userId = getUid();
        final double finalLatitude = latitude;
        final double finalLongitude = longitude;
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewPostActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, title, body, numGuests, date, duration, tags, imgURL, address, finalLatitude, finalLongitude);
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body, String numGuests, String date, String duration, String tags, String imgURL, String address, double latitude, double longitude ) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        //    public Experience(String uid, String title, String author, String description, String numGuests, String duration) {

        Experience post = new Experience(userId, title, username, body, numGuests, date, duration, tags, imgURL, address);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        geoFire.setLocation(key, new GeoLocation(latitude, longitude));

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}

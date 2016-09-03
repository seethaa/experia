package com.experia.experia.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.util.HashMap;
import java.util.Map;

import adapters.LockableViewPager;
import adapters.SmartFragmentStatePagerAdapter;
import fragments.CreateExNameDescriptionPhotoFragment;
import fragments.CreateExReviewFragment;
import fragments.CreateExTimeLocationFragment;
import models.Experience;
import models.User;

public class NewPostActivity extends BaseActivity implements CreateExNameDescriptionPhotoFragment.OnNameDescriptionPhotoCompleteListener,
        CreateExTimeLocationFragment.OnWhereAndWhenCompleteListener,
        CreateExReviewFragment.OnReviewCompleteListener{

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference geoRef;
    private GeoFire geoFire;
    // [END declare_database_ref]

    private EditText etTitle;
    private EditText etBody;
    private EditText etNumGuests;
    private EditText etDate;
    private EditText etDuration;
    private EditText etTags;
    private EditText etImgURL;
    private EditText etAddress;
    private EditText etType;
    private EditText etLatitude;
    private EditText etLongitude;

    String mUserDisplayName;
    String mTitle;
    String mBody;
    int mNumGuests;
    String mDate;
    String mTime;
    String mDuration;
    String mTags;
    String mImgURL;
    String mAddress;
    int mType;
    double mLatitude;
    double mLongitude;

    Uri mphotoImgUri;

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    public final String APP_TAG = "Experia";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    private MyPagerAdapter mSimpleFragmentAdapter;
    private LockableViewPager mViewPager;
    private ExtensiblePageIndicator extensiblePageIndicator;
    CreateExNameDescriptionPhotoFragment createExNameDescriptionFragment;

    private StorageReference mStorageRef;
    UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_experience);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStorageRef = FirebaseStorage.getInstance().getReference();

//        extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        mSimpleFragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());
//        mSimpleFragmentAdapter.addFragment(CreateExNameDescriptionPhotoFragment.newInstance(R.color.frag1, R.drawable.char1));
//        mSimpleFragmentAdapter.addFragment(CreateExTimeLocationFragment.newInstance(R.color.frag2, R.drawable.char2));
//        mSimpleFragmentAdapter.addFragment(CreateExReviewFragment.newInstance(R.color.frag3, R.drawable.char3));

        // mSimpleFragmentAdapter.addFragment(CreateExTotalTagsTypeFragment.newInstance(R.color.frag2, R.drawable.char2));
//        mSimpleFragmentAdapter.addFragment(CreateExPhotoFragment.newInstance(R.color.frag3, R.drawable.char3));

        mViewPager = (LockableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSimpleFragmentAdapter);
//        extensiblePageIndicator.initViewPager(mViewPager);
        mViewPager.setSwipeable(false);


        mViewPager.setCurrentItem(0);
//        setupViews();

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        geoRef = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        geoFire = new GeoFire(geoRef);



    }

    private void setupViews() {
        setContentView(R.layout.activity_new_post);

        etTitle = (EditText) findViewById(R.id.field_title);
        etBody = (EditText) findViewById(R.id.field_body);
        etNumGuests = (EditText) findViewById(R.id.field_numGuests);
        etDate = (EditText) findViewById(R.id.field_date);
        etDuration = (EditText) findViewById(R.id.field_duration);
        etTags = (EditText) findViewById(R.id.field_tags);
        etImgURL = (EditText) findViewById(R.id.field_imgURL);
        etAddress = (EditText) findViewById(R.id.field_address);
        etType = (EditText) findViewById(R.id.field_type);
        etLatitude = (EditText) findViewById(R.id.field_latitude);
        etLongitude = (EditText) findViewById(R.id.field_longitude);


        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

    }

    private void submitPostTest() {

//        uploadFromUri(mphotoImgUri);

        mDuration = "5 HOURS";

        mLatitude = 1.0;
        mLongitude = 1.0;

//        mNumGuests = 100;
//
//        mDate = "Today";

        // [START single_value_read]
        final String userId = getUid();
        final double finalLatitude = mLatitude;
        final double finalLongitude = mLongitude;
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
                            writeNewPost(userId, user.username, mTitle, mBody, mNumGuests, mDate, mTime, mDuration, mTags, mImgURL, mAddress, mType, finalLatitude, finalLongitude);
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


    private void submitPost() {
        mTitle = etTitle.getText().toString();
        mBody = etBody.getText().toString();
        mNumGuests =  Integer.parseInt(String.valueOf(etNumGuests.getText()));
        mDate = etDate.getText().toString();
        mTime = "9:00";
        mDuration = etDuration.getText().toString();
        mTags = etTags.getText().toString();
        mImgURL = etImgURL.getText().toString();
        mAddress = etAddress.getText().toString();
        mType = Integer.parseInt(String.valueOf(etType.getText()));

        try{
            mLatitude = Double.parseDouble(etLatitude.getText().toString());
        } catch (final NumberFormatException e) {
            mLatitude = 1.0;
        }
        try{
            mLongitude = Double.parseDouble(etLongitude.getText().toString());
        } catch (final NumberFormatException e) {
            mLongitude = 1.0;
        }

        // Title is required
        if (TextUtils.isEmpty(mTitle)) {
            etTitle.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(mBody)) {
            etBody.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(mDuration)) {
            etDuration.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(mTags)) {
            etTags.setError(REQUIRED);
            return;
        }

        // [START single_value_read]
        final String userId = getUid();
        final double finalLatitude = mLatitude;
        final double finalLongitude = mLongitude;
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
                            writeNewPost(userId, user.username, mTitle, mBody, mNumGuests, mDate, mTime, mDuration, mTags, mImgURL, mAddress, mType, finalLatitude, finalLongitude);
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
    private void writeNewPost(String userId, String username, String title, String body, int numGuests, String date, String time, String duration, String tags, String imgURL, String address, int type , double latitude, double longitude ) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        //    public Experience(String uid, String title, String author, String description, String totalSpots, String duration) {

        Experience post = new Experience(userId, title, username, body, numGuests, date, time, duration, tags, imgURL, address, type);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        geoFire.setLocation(key, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });


        mDatabase.updateChildren(childUpdates);
    }




    @Override
    public void onNameDescriptionPhotoCompleted(String title, String description, String imgUrl) {
        mTitle = title;
        mBody = description;
        mImgURL = imgUrl;
        System.out.println("DEBUGGY Experience page 1: " + mTitle + ", " + mBody + " ," + imgUrl);

        mViewPager.setCurrentItem(1);

    }

    @Override
    public void onWhereAndWhenCompleted(String address, String date, String time) {
        mAddress = address;
        mDate = date;
        mTime = time;
        System.out.println("DEBUGGY Experience page 2: " + mAddress + ", " + mDate + ", " + mTime);
//        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);

//        int s = mViewPager.getCurrentItem()+1;
//
//        mViewPager.setCurrentItem(2);

        mViewPager.setCurrentItem(2);

        CreateExReviewFragment fragment = (CreateExReviewFragment) mSimpleFragmentAdapter.getRegisteredFragment(2);
        fragment.populatePreviewFields(mUserDisplayName, mTitle, mBody, mNumGuests, mDate, mTime, mDuration, mTags, mImgURL, mAddress, mType);
//        ((TextView) fragment.getView().findViewById(R.id.tvTitle)).setText("TESTING");

    }



    @Override
    public void OnReviewCompleted(String mUserDisplayName, String mTitle, String mBody, int mNumGuests, String mDate, String mTime, String mDuration, String mTags, String mImgURL, String mAddress, int mType) {

         this.mUserDisplayName = mUserDisplayName;
         this.mTitle = mTitle;
         this.mBody  = mBody;
         this.mNumGuests = mNumGuests;
         this.mDate = mDate;
         this.mTime = mTime;
//        String mTags;
         this.mImgURL = mImgURL;
         this.mAddress = mAddress;
//        int mType;
//        double mLatitude;
//        double mLongitude;
        submitPostTest();
        System.out.println("DEBUGGY: " + mTime);

    }


    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
//                    return CreateExCardFragment.newInstance(R.color.frag1, R.drawable.char1);

                    return CreateExNameDescriptionPhotoFragment.newInstance(R.color.frag1, R.drawable.char1);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CreateExTimeLocationFragment.newInstance(R.color.frag2, R.drawable.char2);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return CreateExReviewFragment.newInstance(R.color.frag3, R.drawable.char3);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

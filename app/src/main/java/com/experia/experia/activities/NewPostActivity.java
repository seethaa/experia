package com.experia.experia.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.experia.experia.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import adapters.SimpleFragmentAdapter;
import adapters.VerticalViewPager;
import fragments.CreateExNameDescriptionFragment;
import fragments.CreateExPhotoFragment;
import fragments.CreateExTimeLocationFragment;
import fragments.CreateExTotalTagsTypeFragment;
import models.Experience;
import models.User;

public class NewPostActivity extends BaseActivity implements CreateExNameDescriptionFragment.OnNameAndDescriptionCompleteListener,
        CreateExTimeLocationFragment.OnWhereAndWhenCompleteListener,
        CreateExPhotoFragment.OnPhotoPickCompleteListener,
        CreateExTotalTagsTypeFragment.OnTotaltagsTypeCompleteListener{

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

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    public final String APP_TAG = "Experia";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    private SimpleFragmentAdapter mSimpleFragmentAdapter;
    private VerticalViewPager mViewPager;
    private ExtensiblePageIndicator extensiblePageIndicator;
    CreateExNameDescriptionFragment createExNameDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_experience);

        extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        mSimpleFragmentAdapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        mSimpleFragmentAdapter.addFragment(CreateExNameDescriptionFragment.newInstance(R.color.frag1, R.drawable.char1));
        mSimpleFragmentAdapter.addFragment(CreateExTimeLocationFragment.newInstance(R.color.frag2, R.drawable.char2));
        mSimpleFragmentAdapter.addFragment(CreateExTotalTagsTypeFragment.newInstance(R.color.frag2, R.drawable.char2));
        mSimpleFragmentAdapter.addFragment(CreateExPhotoFragment.newInstance(R.color.frag3, R.drawable.char3));

        mViewPager = (VerticalViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSimpleFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);
        mViewPager.setPagingEnabled(false);

        //GET ACCESS TO FRAGMENT
        createExNameDescriptionFragment = (CreateExNameDescriptionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentC);


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

        findViewById(R.id.btnImageUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
            }
        });

        findViewById(R.id.btnTakePicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });

        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (data != null) {
//            Uri photoUri = data.getData();
//            // Do something with the photo based on Uri
//            Bitmap selectedImage = null;
//            try {
//                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // Load the selected image into a preview
//            ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
//            ivPreview.setImageBitmap(selectedImage);
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PICK_PHOTO_CODE) {
            if (data != null) {
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(selectedImage);
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }


    private void submitPostTest() {

        mDuration = "5 HOURS";

        mLatitude = 1.0;
        mLongitude = 1.0;


//        // Title is required
//        if (TextUtils.isEmpty(mTitle)) {
//            etTitle.setError(REQUIRED);
//            return;
//        }
//
//        // Body is required
//        if (TextUtils.isEmpty(mBody)) {
//            etBody.setError(REQUIRED);
//            return;
//        }
//
//        if (TextUtils.isEmpty(mDuration)) {
//            etDuration.setError(REQUIRED);
//            return;
//        }
//        if (TextUtils.isEmpty(mTags)) {
//            etTags.setError(REQUIRED);
//            return;
//        }

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
    public void onNameDescriptionCompleted(String field_title, String field_body) {
        mTitle = field_title;
        mBody = field_body;
        System.out.println("DEBUGGY Experience page 1: " + mTitle + ", " + mBody);

        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
    }


    @Override
    public void onWhereAndWhenCompleted(String address, String date, String time) {
        mAddress = address;
        mDate = date;
        mTime = time;
        System.out.println("DEBUGGY Experience page 2: " + mAddress + ", " + mDate + ", " + mTime);
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);

    }


    @Override
    public void onTotaltagsTypeCompleted(int total, String tags, int type) {
        mNumGuests = total;
        mTags = tags;
        mType = type;

        System.out.println("DEBUGGY Experience page 3: " + mNumGuests + ", " + mTags + ", " + mType);

        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);

    }

    @Override
    public void onSavePhotosCompleted(String imgString) {
        mImgURL = "http://peoplehouse.org/wp-content/uploads/2015/01/surfing-beach-wallpaper_90085-1920x1200.jpg";
        System.out.println("DEBUGGY Experience page 4: " + mImgURL);

        submitPostTest();

    }

}

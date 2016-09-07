package com.experia.experia.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.experia.experia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import fragments.DetailFragment;
import models.Experience;
import models.User;

public class PostDetailActivity extends BaseActivity {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_ID = "post_id";

    public DatabaseReference mPostReference;
    protected DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostId;

    private Experience experience;

    private TextView mTitleView;

    private ImageView mImageViewExperience;

    User userClicked;
    String userImgURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_detail_with_tabs);



        // Get post key from intent
        mPostId = getIntent().getStringExtra(EXTRA_POST_ID);
        if (mPostId == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        getUserClicked();

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostId);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostId);

        // Initialize Views
        mTitleView = (TextView) findViewById(R.id.tvExperienceTitle);

        mImageViewExperience = (ImageView) findViewById(R.id.ivExperienceImage);


    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                 experience = dataSnapshot.getValue(Experience.class);


                createDetailFragment(experience);
                // [START_EXCLUDE]
//                mAuthorView.setText(post.author);
                mTitleView.setText(experience.title);

                getSupportActionBar().setTitle(experience.title);

                String img = experience.imgURL;
                if (!TextUtils.isEmpty(experience.imgURL)) {
                    Glide.with(getApplicationContext()).load(img).centerCrop().placeholder(R.drawable.pattern)
                            .into(mImageViewExperience);

                }
                else{

                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
//        mAdapter = new CommentAdapter(this, mCommentsReference);
//        mCommentsRecycler.setAdapter(mAdapter);

    }

    public void getUserClicked() {
        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").child(getUid());


        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userClicked = dataSnapshot.getValue(User.class);

                System.out.println("DEBUGGY USER CLICKED " + userClicked.getDisplayName() + " " +userClicked.getEmail());

                userImgURL = userClicked.getImageURL();

//                mName.setText(userClicked.username);
//                if (userClicked.getImageURL()!=null && !TextUtils.isEmpty(userClicked.getImageURL())) {
//                    Glide.with(getApplicationContext()).load(userClicked.getImageURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImage) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            mProfileImage.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createDetailFragment(Experience experience) {
        DetailFragment fragmentUserTimeline = DetailFragment.newInstance(experience, mPostId, userImgURL);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentUserTimeline);
        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
//
//        // Clean up comments listener
//        mAdapter.cleanupListener();
    }



}

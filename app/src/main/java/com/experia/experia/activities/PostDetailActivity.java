package com.experia.experia.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.experia.experia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fragments.DetailFragment;
import models.Experience;

public class PostDetailActivity extends BaseActivity {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_ID = "post_id";

    public DatabaseReference mPostReference;
    protected DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostId;

    private Experience experience;
//    private CommentAdapter mAdapter;

    private TextView mTitleView;

    private TextView mSpotsAvailable;
    private ImageView mImageViewExperience;
//    private TextView mAddress;


    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_detail_with_tabs);



        // Get post key from intent
        mPostId = getIntent().getStringExtra(EXTRA_POST_ID);
        if (mPostId == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostId);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostId);

        // Initialize Views
//        mAuthorView = (TextView) findViewById(R.id.tvHostName);
        mTitleView = (TextView) findViewById(R.id.tvExperienceTitle);
//        mBodyView = (TextView) findViewById(R.id.tvDescription);
//        mAddress = (TextView) findViewById(R.id.tvLocationAddress);
//        mDate = (TextView) findViewById(R.id.tvDate);
//        mSpotsAvailable = (TextView) findViewById(R.id.tvSpotsLeft);
        mImageViewExperience = (ImageView) findViewById(R.id.ivExperienceImage);
//
//
//        mCommentField = (EditText) findViewById(R.id.field_comment_text);
//        mCommentButton = (Button) findViewById(R.id.button_post_comment);
//        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
//
//        mCommentButton.setOnClickListener(this);
//        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));




//        setupFragmentTabs();

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

//                mBodyView.setText(post.description);
//                mAuthorView.setText(post.author);
//                mDate.setText(post.date);
//                mSpotsAvailable.setText(experience.getSpotsLeft() + " spots left");
//                mAddress.setText(post.address);


                String img = experience.imgURL;
                if (!TextUtils.isEmpty(experience.imgURL)) {
                    Glide.with(getApplicationContext()).load(img).centerCrop().placeholder(R.drawable.pattern)
                            .into(mImageViewExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))

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

    private void createDetailFragment(Experience experience) {
        DetailFragment fragmentUserTimeline = DetailFragment.newInstance(experience, mPostId);
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

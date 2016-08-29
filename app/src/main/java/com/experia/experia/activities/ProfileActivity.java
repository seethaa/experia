package com.experia.experia.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.experia.experia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import models.User;

public class ProfileActivity extends AppCompatActivity{
    FirebaseAuth auth;
    FirebaseUser user;
    Uri profilePhotoUrl;
    String displayName;
    String email;

    private TextView mName;
    private TextView mTitleView;
    private ImageView mProfileImage;
    public DatabaseReference mUserReference;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users");

        mName = (TextView) findViewById(R.id.tvName);
        mTitleView = (TextView) findViewById(R.id.tvBio);
        mProfileImage = (ImageView)  findViewById(R.id.ivProfileImage);

        User userClicked = getUserClicked();

//        Uri userClickedPhotoURL = userClicked.getPhotoUrl();

        auth = FirebaseAuth.getInstance();
        if(auth !=null)
        {
            user = auth.getCurrentUser();
            profilePhotoUrl = auth.getCurrentUser().getPhotoUrl();
//            Toast.makeText(ProfileActivity.this, "Profile Url: " + profilePhotoUrl.toString(), Toast.LENGTH_LONG).show();
            displayName = user.getDisplayName();
            email = user.getEmail();
        }

        mName.setText(displayName);
        if (!TextUtils.isEmpty(profilePhotoUrl.toString())) {
            Glide.with(this).load(profilePhotoUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mProfileImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        }






    }

    public User getUserClicked() {

        return null;
    }

    public Query getUserQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}

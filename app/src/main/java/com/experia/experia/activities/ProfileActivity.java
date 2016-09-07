package com.experia.experia.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.experia.experia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import models.User;

public class ProfileActivity extends AppCompatActivity {
//    FirebaseAuth auth;
//    FirebaseUser user;
    Uri profilePhotoUrl;
    String displayName;
    String email;
    String currUsername;
    String currProfileUri;
    String userID;

    private TextView mName;
    private TextView mTitleView;
    private ImageView mProfileImage;
    public DatabaseReference mUserReference;
    User userClicked;

    private static final String TAG = "ProfileActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = getIntent().getStringExtra("userID");
        System.out.println("DEBUGGY USERNAME: " + userID);

        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users");

        mName = (TextView) findViewById(R.id.tvName);
        mTitleView = (TextView) findViewById(R.id.tvBio);
        mProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        getUserClicked();

    }

    public void getUserClicked() {
        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID);


        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userClicked = dataSnapshot.getValue(User.class);

                System.out.println("DEBUGGY USER CLICKED " + userClicked.getDisplayName() + " " +userClicked.getEmail());


                mName.setText(userClicked.username);
                if (userClicked.getImageURL()!=null && !TextUtils.isEmpty(userClicked.getImageURL())) {
                    Glide.with(getApplicationContext()).load(userClicked.getImageURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImage) {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_filter).setVisible(false);
        return true;
    }


    }


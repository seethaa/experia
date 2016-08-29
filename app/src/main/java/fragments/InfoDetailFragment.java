package fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.experia.experia.R;

import org.parceler.Parcels;

import models.Experience;

public class InfoDetailFragment extends Fragment {
    private static final String TAG = "InfoDetailActivity";
    private TextView mAuthorView;
//    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mAddress;
    private TextView mDate;

    private TextView mSpotsAvailable;
    private TextView mNumGoing;

    //    private ImageView mImageViewExperience;
    public static Experience mExperience;


    public static InfoDetailFragment newInstance(Experience experience) {


        InfoDetailFragment fg = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("experience", Parcels.wrap(experience));
        fg.setArguments(args);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExperience =  Parcels.unwrap(getArguments().getParcelable("experience"));




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        // Initialize Views
        mAuthorView = (TextView) view.findViewById(R.id.tvHostName);
//        mTitleView = (TextView) view.findViewById(R.id.tvExperienceTitle);
        mBodyView = (TextView) view.findViewById(R.id.tvDescription);
        mAddress = (TextView) view.findViewById(R.id.tvLocationAddress);
        mDate = (TextView) view.findViewById(R.id.tvDate);
        mSpotsAvailable = (TextView) view.findViewById(R.id.tvSpotsLeft);
        mNumGoing = (TextView) view.findViewById(R.id.tvNumGoing);

//        mImageViewExperience = (ImageView) view.findViewById(R.id.ivExperienceImage);


        mAuthorView.setText(mExperience.author);
//        mTitleView.setText(mExperience.title);
        mBodyView.setText(mExperience.description);
        mAuthorView.setText(mExperience.author);
        mDate.setText(mExperience.date);
        mSpotsAvailable.setText(mExperience.getSpotsLeft() + " spots left");
        mNumGoing.setText(mExperience.joinCount + " going");
        mAddress.setText(mExperience.address);


//        String img = mExperience.imgURL;
//        if (!TextUtils.isEmpty(mExperience.imgURL)) {
//            Glide.with(getActivity()).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
//                    .into(mImageViewExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))

            return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Add value event listener to the post
//        // [START post_value_event_listener]
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Experience post = dataSnapshot.getValue(Experience.class);
//                // [START_EXCLUDE]
////                mAuthorView.setText(post.author);
//                mTitleView.setText(post.title);
//                mBodyView.setText(post.description);
//                mAuthorView.setText(post.author);
//                mDate.setText(post.date);
//                mSpotsAvailable.setText(post.totalSpots + " spots left");
//                mAddress.setText(post.address);
//
//
//                String img = post.imgURL;
//                if (!TextUtils.isEmpty(post.imgURL)) {
//                    Glide.with(getActivity()).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
//                            .into(mImageViewExperience);
////            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))
//
//
//                }
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // [START_EXCLUDE]
//                Toast.makeText(getActivity(), "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
//                // [END_EXCLUDE]
//            }
//        };
////        mPostReference.addValueEventListener(postListener);
////        // [END post_value_event_listener]
////
////        // Keep copy of post listener so we can remove it when app stops
////        mPostListener = postListener;
////
////        // Listen for comments
////        mAdapter = new CommentAdapter(this, mCommentsReference);
////        mCommentsRecycler.setAdapter(mAdapter);
//    }
}

package fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.experia.experia.R;

import models.Experience;

public class CreateExCardFragment extends Fragment {
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


    public static CreateExCardFragment newInstance(int color, int icon) {
        CreateExCardFragment fragment = new CreateExCardFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, icon);
//        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mExperience =  Parcels.unwrap(getArguments().getParcelable("experience"));




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_create_experience, container, false);

//        // Initialize Views
//        mAuthorView = (TextView) view.findViewById(R.id.tvHostName);
////        mTitleView = (TextView) view.findViewById(R.id.tvExperienceTitle);
//        mBodyView = (TextView) view.findViewById(R.id.tvDescription);
//        mAddress = (TextView) view.findViewById(R.id.tvLocationAddress);
//        mDate = (TextView) view.findViewById(R.id.tvDate);
//        mSpotsAvailable = (TextView) view.findViewById(R.id.tvSpotsLeft);
//        mNumGoing = (TextView) view.findViewById(R.id.tvNumGoing);
//
////        mImageViewExperience = (ImageView) view.findViewById(R.id.ivExperienceImage);
//
//
//        mAuthorView.setText(mExperience.author);
////        mTitleView.setText(mExperience.title);
//        mBodyView.setText(mExperience.description);
//        mAuthorView.setText(mExperience.author);
//        mDate.setText(mExperience.date);
//        mSpotsAvailable.setText(mExperience.getSpotsLeft() + " spots left");
//        mNumGoing.setText(mExperience.joinCount + " going");
//        mAddress.setText(mExperience.address);
//
//
//        mAuthorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                intent.putExtra("userID", mExperience.uid);
//                startActivity(intent);
//            }
//        });

//        String img = mExperience.imgURL;
//        if (!TextUtils.isEmpty(mExperience.imgURL)) {
//            Glide.with(getActivity()).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
//                    .into(mImageViewExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))

        return view;
    }

}

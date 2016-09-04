package fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.experia.experia.R;
import com.experia.experia.activities.ProfileActivity;

import org.parceler.Parcels;

import models.Experience;

public class InfoDetailFragment extends Fragment {
    private static final String TAG = "InfoDetailActivity";
    private TextView mAuthorView;
//    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mAddress;
    private TextView mAddressName;
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
        mAddressName = (TextView) view.findViewById(R.id.tvLocationName);
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
//        mAddress.setText(mExperience.address);
        mAddressName.setText(mExperience.addressName);
        mAddressName.setText(mExperience.addressName);

        mAddress.setClickable(true);
        SpannableString content = new SpannableString(mExperience.address);
        content.setSpan(new UnderlineSpan(), 0, mExperience.address.length(), 0);
        mAddress.setText(content);
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
                        +mAddress.getText().toString()));
                startActivity(geoIntent);
            }
        });

        mAuthorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("userID", mExperience.uid);
                startActivity(intent);
            }
        });

//        String img = mExperience.imgURL;
//        if (!TextUtils.isEmpty(mExperience.imgURL)) {
//            Glide.with(getActivity()).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
//                    .into(mImageViewExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))

            return view;
    }

}

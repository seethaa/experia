package fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.experia.experia.R;
import com.experia.experia.activities.ProfileActivity;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import models.Experience;

public class InfoDetailFragment extends Fragment {
    private static final String TAG = "InfoDetailActivity";
    private TextView mAuthorView;
    private TextView mBodyView;
    private TextView mAddress;
    private TextView mAddressName;
    private TextView mDate;
    private TextView mTimeLeft;
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
        mBodyView = (TextView) view.findViewById(R.id.tvDescription);
        mAddress = (TextView) view.findViewById(R.id.tvLocationAddress);
        mAddressName = (TextView) view.findViewById(R.id.tvLocationName);
        mDate = (TextView) view.findViewById(R.id.tvDate);
        mTimeLeft = (TextView) view.findViewById(R.id.tvDayAtTime);
        mSpotsAvailable = (TextView) view.findViewById(R.id.tvSpotsLeft);
        mNumGoing = (TextView) view.findViewById(R.id.tvNumGoing);


        mAuthorView.setText(mExperience.author);
        mBodyView.setText(mExperience.description);
        mAuthorView.setText(mExperience.author);
        mDate.setText(mExperience.date);
        String date = mExperience.date;
        String time = mExperience.time;
        Log.d(TAG, date + " " + time);
        Log.d(TAG, "relativeTimeAgo=" + getRelativeTimeAgo(date, time));
        final Countdown timer = new Countdown(getRelativeTimeAgo(date, time),1000); //first parameter number of milliseconds in future
        timer.start();
        mSpotsAvailable.setText(mExperience.getSpotsLeft() + " spots left");
        mNumGoing.setText(mExperience.joinCount + " going");
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

        return view;
    }


    public class Countdown extends CountDownTimer {

        public Countdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override public void onFinish() {

            mTimeLeft.setText("Just missed!");
//            mTimeLeft.setTextColor(getResources().getColor(R.color.mp_brown, null));
        }

        @Override public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("Event starts in %02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            mTimeLeft.setText(hms);
//            if(millisUntilFinished <= 1000) {//less than 15 mins
//                mTimeLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
//            }
//            else{ //greater than or equal to 15 mins
//                mTimeLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//            }
        }
    }
    public static long getRelativeTimeAgo(String date, String time) {
        String twitterFormat = "MM/dd/yyyy HH:mm";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        long relativeTime = 0;
        try {
            long dateMillis = sf.parse(date+ " "+time).getTime();
            relativeTime = dateMillis - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeTime;
    }
}

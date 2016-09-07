package fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.experia.experia.R;
import com.google.firebase.database.DatabaseReference;

import org.parceler.Parcels;

import models.Experience;

public class DetailFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";


    public static final String EXTRA_POST_KEY = "post_key";
    private String mPostKey;

    private InfoDetailFragment infoDetailFragment;
    private ReviewsDetailFragment reviewsDetailFragment;

    private static Experience mExperience;
    private String imgURL;

    public DatabaseReference mPostReference;
    protected DatabaseReference mCommentsReference;

    public static DetailFragment newInstance(Experience experience, String postkey, String userImgURL) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("experience", Parcels.wrap(experience));
        args.putParcelable("postkey", Parcels.wrap(postkey));
        args.putParcelable("imgURL", Parcels.wrap(postkey));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExperience =  Parcels.unwrap(getArguments().getParcelable("experience"));
        mPostKey = Parcels.unwrap(getArguments().getParcelable("postkey"));
        imgURL = Parcels.unwrap(getArguments().getParcelable("imgURL"));

//        mExperience = (Experience) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("experience"));

        System.out.println("DEBUGGY IMG url: " + imgURL);
        // Initialize Database
//        mPostReference = FirebaseDatabase.getInstance().getReference()
//                .child("posts").child(mPostKey);
//        mCommentsReference = FirebaseDatabase.getInstance().getReference()
//                .child("post-comments").child(mPostKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_bottom, container, false);

        setupFragmentTabs(view);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }


    private void setupFragmentTabs(View v) {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.vpDetailBottom);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Info", "Comments", "Going" };
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0){
                infoDetailFragment = InfoDetailFragment.newInstance(mExperience);
                return infoDetailFragment;
            }
            else if (position ==1){
                reviewsDetailFragment = ReviewsDetailFragment.newInstance(mExperience, mPostKey, imgURL);
                return reviewsDetailFragment;
            }
            else{
                infoDetailFragment = InfoDetailFragment.newInstance(mExperience);
                return infoDetailFragment;
            }        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
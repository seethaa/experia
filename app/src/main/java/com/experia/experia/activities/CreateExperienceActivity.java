package com.experia.experia.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.experia.experia.R;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import adapters.SimpleFragmentAdapter;
import adapters.VerticalViewPager;
import fragments.CreateExNameDescriptionFragment;
import fragments.CreateExPhotoFragment;
import fragments.CreateExTimeLocationFragment;


public class CreateExperienceActivity extends AppCompatActivity implements CreateExNameDescriptionFragment.OnNameAndDescriptionCompleteListener {

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
        mSimpleFragmentAdapter.addFragment(CreateExPhotoFragment.newInstance(R.color.frag3, R.drawable.char3));

        mViewPager = (VerticalViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSimpleFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);

        //GET ACCESS TO FRAGMENT
        createExNameDescriptionFragment = (CreateExNameDescriptionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentC);

    }


    @Override
    public void onNameDescriptionCompleted(String field_title, String field_body) {

    }
}
package com.experia.experia.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.experia.experia.R;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import adapters.SimpleFragmentAdapter;
import fragments.CreateExNameDescriptionFragment;
import fragments.CreateExPhotoFragment;
import fragments.CreateExTimeLocationFragment;

/**
 * Created by doc_dungeon on 8/27/16.
 */
public class CreateExperienceActivity extends AppCompatActivity {

    private SimpleFragmentAdapter mSimpleFragmentAdapter;
    private ViewPager mViewPager;
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

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSimpleFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);

        //GET ACCESS TO FRAGMENT
        createExNameDescriptionFragment = (CreateExNameDescriptionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentC);

    }


}
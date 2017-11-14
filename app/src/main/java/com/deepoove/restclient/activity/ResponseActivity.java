package com.deepoove.restclient.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import com.deepoove.restclient.R;
import com.deepoove.restclient.fragment.ResponseBodyFragment;
import com.deepoove.restclient.fragment.ResponseGeneralFragment;
import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.ui.AdvancedViewPager;

public class ResponseActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AdvancedViewPager mViewPager;
    private RestTest restTest;
    private RemoteResponse response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        restTest = (RestTest) getIntent().getSerializableExtra("restTest");
        response = (RemoteResponse) getIntent().getSerializableExtra("response");


        mViewPager = (AdvancedViewPager) findViewById(R.id.container);
        mViewPager.setEnable(false);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        int code = response.getStatusCode();
        if (code >= 200 && code <= 299) {
            toolbar.setBackgroundResource(R.color.get);
            tabLayout.setBackgroundResource(R.color.get);
        } else {
            toolbar.setBackgroundResource(R.color.error);
            tabLayout.setBackgroundResource(R.color.error);
        }
        if (-1 == response.getStatusCode()) {
            ab.setTitle(response.getResponseMessage());
        } else {
            ab.setTitle(response.getStatusCode() + " " + response.getResponseMessage());
        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (0 == position) return ResponseBodyFragment.newInstance(response, restTest);
            return ResponseGeneralFragment.newInstance(response, restTest);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BODY";
                case 1:
                    return "GENERAL";
            }
            return null;
        }
    }
}

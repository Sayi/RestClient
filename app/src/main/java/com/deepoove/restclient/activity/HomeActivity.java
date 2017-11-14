package com.deepoove.restclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.deepoove.restclient.R;
import com.deepoove.restclient.adapter.PagerAdapter;
import com.deepoove.restclient.fragment.RestTestFragment;
import com.deepoove.restclient.ui.AdvancedViewPager;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    AdvancedViewPager mViewPager;
    NavigationView navigationView;
    android.support.v7.app.ActionBar actionBar;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_rest);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_rest);


        mViewPager = (AdvancedViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setTitle(navigationView.getMenu().getItem(position).getTitle());
                actionBar.setHomeAsUpIndicator(R.drawable.ic_rest);
                navigationView.getMenu().getItem(position).setChecked(true);
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (1 == mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(0);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast makeText = Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT);
            makeText.setDuration(Toast.LENGTH_SHORT);
            makeText.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rest, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                getMenuInflater().inflate(R.menu.rest, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.rest, menu);
                break;

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (DrawerLayout.LOCK_MODE_LOCKED_CLOSED != mDrawerLayout.getDrawerLockMode(Gravity.LEFT)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_add_test:
                Intent intent = new Intent(this, AddRestActivity.class);
                startActivityForResult(intent, 300, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 300) {
            refreshRestList();
        }
    }

    private void refreshRestList() {
        String name = makeFragmentName(mViewPager.getId(), 0);
        Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);
        if (viewPagerFragment != null && mViewPager.getCurrentItem() == 0) {
            RestTestFragment fragment = (RestTestFragment) viewPagerFragment;
            fragment.refresh();
        }
    }


    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_rest) {
            mViewPager.setCurrentItem(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean doubleBackToExitPressedOnce = false;


    public void navigateToAbout(View view) {

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


}

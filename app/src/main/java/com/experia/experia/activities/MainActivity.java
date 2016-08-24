package com.experia.experia.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.experia.experia.R;
import com.google.android.youtube.player.YouTubePlayerFragment;

import fragments.BookmarksFragment;
import fragments.CategoryFragment;
import fragments.LocationSettingsFragment;
import fragments.MyPostsFragment;
import fragments.RecentPostsFragment;
import fragments.MediaControllerFragment;
import fragments.YouTubeFragment;
import util.Constants;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    //custom notification
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(mDrawerToggle);

        mNavDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(mNavDrawer);

//        mNavDrawer.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new RecentPostsFragment()).commit();
        setTitle("Experiences around me");

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });

        // send notification to user
        createBigPictureStyleNoti(72,R.drawable.ic_logo_placeholder, "Oktoberfest!",
                "Live Band playing at the Roxy");


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_location_fragment:
                fragmentClass = LocationSettingsFragment.class;
                break;
            case R.id.nav_category_fragment:
                fragmentClass = CategoryFragment.class;
                break;
            case R.id.nav_bookmarks_fragment:
                fragmentClass = BookmarksFragment.class;
                break;
            case R.id.nav_account_fragment:
                fragmentClass = RecentPostsFragment.class;
                break;
            case R.id.nav_profile_fragment:
                fragmentClass = MyPostsFragment.class;
                break;
            case R.id.nav_video_fragment: //test video player
                fragmentClass = MediaControllerFragment.class;
                break;
            case R.id.nav_youtube_fragment: //test video player
                fragmentClass = YouTubeFragment.class;
                break;
            default:
                fragmentClass = RecentPostsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //BigPicture Style Layout
    private void createBigPictureStyleNoti (int nId, int iconRes, String title, String body) {
        // First let's define the intent to trigger when notification is selected
        // Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(this, MainActivity.class);
        // Next, let's turn this into a PendingIntent using
        //   public static PendingIntent getActivity(Context context, int requestCode,
        //       Intent intent, int flags)
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        // .setLargeIcon expects a bitmap
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_bitmap_lg_crown);

        // Big picture for style
        final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.big_oktober_fest);

        //Items visible in Collapsed InboxView
        String contentText = "Learn archery from the best instructors who'll take the time to get " +
                "you up and running and see you hitting the target.";
        String subText = "Located in SoMa";

        //Items visible in Expanded InboxView
        String bigContentText = "Learn the archery of Robin Hood";
        String summaryText = "Serious fun with bows & arrows";

        // InboxStyle Notification
        Notification bigPictStyleNotification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(contentText)
                .addAction(R.drawable.ic_phone_call,"ACTION",pIntent)
                .addAction(R.drawable.ic_calendar,"ACTION",pIntent)
                .addAction(R.drawable.ic_invite_friends,"ACTION",pIntent)
                .setSmallIcon(iconRes)  //miniature
                .setLargeIcon(largeIcon)
                .setSubText(subText)
                .setAutoCancel(true) // Hides the notification after its been selected
                .setPriority(Constants.NOTICE_PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(bigContentText)
                        .setSummaryText(summaryText)
                        .bigPicture(picture))
                .build();

        // Get the notification manager system service
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, bigPictStyleNotification);
    }
}
package com.codepath.apps.mysimpletweets.Activity;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.TweetsPagerAdapter;
import com.codepath.apps.mysimpletweets.Adapters.ProfilePagerAdapter;
import com.codepath.apps.mysimpletweets.Fragments.ProfileHeaderFragment;
import com.codepath.apps.mysimpletweets.Fragments.TweetComposeFragment;
import com.codepath.apps.mysimpletweets.Fragments.UserProfileTimeFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;

public class ProfileActivity extends AppCompatActivity implements TweetComposeFragment.OnFragmentInteractionListener{
    final static String TAG = "Profile Activity";
    ProfilePagerAdapter profilePagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar)findViewById(R.id.profileToolbarId);
        this.setSupportActionBar(toolbar);

        //Get the screen name
        String screenName = getIntent().getStringExtra("screen_name");
        if(screenName != null) getSupportActionBar().setTitle("@"+screenName);
        if(savedInstanceState == null) {
            ProfileHeaderFragment profileHeaderFragment = ProfileHeaderFragment.newInstance(screenName);
            //Dynamically display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.FLHeaderContainerId, profileHeaderFragment);

            ViewPager vpPager = (ViewPager)findViewById(R.id.profileViewpagerId);
            PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.profileTabsId);

            profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
            profilePagerAdapter.setScreenName(screenName);
            Log.d(TAG, "screenName is "+screenName);
            vpPager.setAdapter(profilePagerAdapter);
            tabStrip.setViewPager(vpPager);

            ft.commit();
        }
    }
    public  void onFinishEditDialog(String newTweet, User user) {

    }

}

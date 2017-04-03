package com.codepath.apps.mysimpletweets.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.Fragments.FollowersProfileFragment;
import com.codepath.apps.mysimpletweets.Fragments.FollowingProfileFragment;
import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.UserProfileTimeFragment;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Tweets","Followers","Following"};
    private String screenName;

    public void setScreenName(String screenName) {this.screenName = screenName;}

    //Adapter gets the manger insert or remove fragment from activity
    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // The order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            UserProfileTimeFragment userProfileTimeFragment = UserProfileTimeFragment.newInstance(screenName, false);
            return userProfileTimeFragment;

        } else if(position == 1){
            FollowersProfileFragment followersProfileFragment = FollowersProfileFragment.newInstance(screenName, false);
            return followersProfileFragment;

        } else if(position == 2){
            FollowingProfileFragment followingProfileFragment = FollowingProfileFragment.newInstance(screenName, false);
            return followingProfileFragment;

        } else {
            UserProfileTimeFragment userProfileTimeFragment = UserProfileTimeFragment.newInstance(screenName, false);
            return userProfileTimeFragment;
        }
    }
    //Return the tab title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    //Number of fragment
    @Override
    public int getCount() {
        return tabTitles.length;
    }

}

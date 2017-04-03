package com.codepath.apps.mysimpletweets.Activity.ActivityTimeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;

import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by jsaluja on 3/30/2017.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Home", "Mentions"};
    private long sinceId = -1, maxId = -1;

    public void setSinceId(long sinceId) {this.sinceId = sinceId;}

    public void setMaxId(long maxId) {this.maxId = maxId;}

    //Adapter gets the manger insert or remove fragment from activity
    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // The order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
            homeTimelineFragment.setSinceId(sinceId);
            homeTimelineFragment.setMaxId(maxId);
            return homeTimelineFragment;
        } else if(position == 1){
            MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();
            mentionsTimelineFragment.setSinceId(sinceId);
            mentionsTimelineFragment.setMaxId(maxId);
            return mentionsTimelineFragment;
        } else {
            HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
            homeTimelineFragment.setSinceId(sinceId);
            homeTimelineFragment.setMaxId(maxId);
            return homeTimelineFragment;
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

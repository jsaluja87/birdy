package com.codepath.apps.mysimpletweets.models.sqlitehelper;

import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsaluja on 3/26/2017.
 */

public class OfflineTweetClass {
    //Retrieve saved list from sq table
    static Organization organization = new Organization();
    final static String TAG = "Offline_tweet_class";
    private ArrayList<Tweet> mTweets;
    private TweetsRecyclerAdapter mTweetsRecyclerAdapter;

    public OfflineTweetClass(ArrayList<Tweet> tweets, TweetsRecyclerAdapter tweetsRecyclerAdapter) {
        mTweets = tweets;
        mTweetsRecyclerAdapter = tweetsRecyclerAdapter;
    }


    public void loadOfflineTweets() {
        List<Organization> organizationList = SQLite.select().from(Organization.class).queryList();

        Log.d(TAG,"Came to Db load "+organizationList.size());
        if (organizationList.size() > 0) {
            for (int i = 0; i < organizationList.size(); i++) {
                Log.d(TAG, "organization body is" + organizationList.get(i).getBody());
                Tweet tweet = new Tweet();
                if(organizationList.get(i).getBody() != null) tweet.setBody(organizationList.get(i).getBody());
                if(tweet.getUser() != null) {
                    if (organizationList.get(i).getName() != null) tweet.getUser().setName(organizationList.get(i).getName());
                    if (organizationList.get(i).getScreenName() != null) tweet.getUser().setScreenName(organizationList.get(i).getScreenName());
                }
                if(organizationList.get(i).getCreatedAt() != null) tweet.setCreatedAt(organizationList.get(i).getCreatedAt());

                mTweets.add(tweet);
            }
            mTweetsRecyclerAdapter.notifyItemRangeInserted(mTweetsRecyclerAdapter.getItemCount(), mTweets.size());
        }
    }

    public void saveTweetsToDB() {
        Log.d(TAG,"Came to Db save "+mTweets.size());
        for(int i = 0; i<mTweets.size();i++) {
            organization.setId(i);
            organization.setBody(mTweets.get(i).getBody());
            organization.setCreatedAt(mTweets.get(i).getCreatedAt());
            if(mTweets.get(i).getUser() != null) {
                organization.setName(mTweets.get(i).getUser().getName());
                organization.setScreenName(mTweets.get(i).getUser().getScreenName());
            }
            organization.save();
        }
    }
}

package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jsaluja on 3/29/2017.
 */

public class UserProfileTimeFragment extends TweetListFragment {
    private TwitterClient client;
    private final static String TAG = "user_profile_fragment";
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserProfileTimeFragment newInstance(String screenName, boolean insertAtTop) {
        UserProfileTimeFragment userFragment = new UserProfileTimeFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        args.putBoolean("insert_at_top", insertAtTop);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline() {
        String screenName = getArguments().getString("screen_name");
        boolean insertAtTop = getArguments().getBoolean("insert_at_top");
        client.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //JSON HERE
                if (insertAtTop) {
                    int old_tweets_size = tweets.size();
                    tweets.addAll(0, Tweet.fromJSONArray(response));
                    int new_tweets_size = tweets.size();
                    tweetsRecyclerAdapter.notifyItemRangeInserted(0, (new_tweets_size - old_tweets_size));
                } else {
                //    addAll(Tweet.fromJSONArray(response));
                    tweets.addAll(Tweet.fromJSONArray(response));
                    Log.d(TAG, "adapter size "+ tweetsRecyclerAdapter.getItemCount()+ "tweets size " + tweets.size());
                    tweetsRecyclerAdapter.notifyItemRangeInserted(tweetsRecyclerAdapter.getItemCount(), tweets.size());
                }
                OfflineTweetClass saveOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                Log.d(TAG, "Before saving the tweets" + tweets.size());
                saveOfflineTweetClass.saveTweetsToDB();

                Log.d(TAG, "Response is " + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Response failed");

                InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
                if (!internetAlertDialogue.isNetworkAvailable()) {
                    internetAlertDialogue.alert_user("Network Issue", "Please connect the device to an internet network!");
                } else {
                    if (!internetAlertDialogue.isOnline()) {
                        internetAlertDialogue.alert_user("Network Issue", "Device network does not have internet access!");
                    } else {
                        Log.d("DEBUG", "!!!Device has internet access!!!");
                    }
                }

                if (tweets.size() == 0) {
                    Toast.makeText(mContext, "Tweet fetch failed!. Loading old saved Tweets!", Toast.LENGTH_LONG).show();
                    //Loading from DB. compared size to make sure the data is only loaded once
                    OfflineTweetClass getOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                    getOfflineTweetClass.loadOfflineTweets();
                }

            }
        }, screenName);
    }
}
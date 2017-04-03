package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
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
    private final static String TAG = "User profile fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  super.onCreateView(inflater, container, savedInstanceState);

        client = TwitterApplication.getRestClient();
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet()) {
            populateTimeline(sinceId, maxId);
        }

        lvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                maxId = tweets.get(tweets.size() - 1).getUid() - 1;
                if(internetAlertDialogue.checkForInternet()) {
                    populateTimeline(sinceId, maxId);
                }
                sinceId = -1;
                maxId = -1;
            }
        });

        return view;
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

    public void populateTimeline(long localSinceId, long localMaxId) {

        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", "Response failed");
                if(statusCode == 200) {
                    alert_user("Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                }
            }
        }, screenName, localSinceId, localMaxId);

        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }
}
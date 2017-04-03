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
 * Created by jsaluja on 3/28/2017.
 */

public class MentionsTimelineFragment extends TweetListFragment {
    private TwitterClient client;
    private final static String TAG = "Mentions fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Very important step to first call onCreateView of the parent in order to not get null on views
        View view = super.onCreateView(inflater, container, savedInstanceState);

        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        client = TwitterApplication.getRestClient();

        if(internetAlertDialogue.checkForInternet()) {
            int tweetArraySize = this.tweets.size();
            this.tweets.clear();
            tweetsRecyclerAdapter.notifyItemRangeRemoved(0, tweetArraySize);
            populateTimeline(sinceId, maxId, false);
        }

        lvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                maxId = tweets.get(tweets.size() - 1).getUid();
                Log.d(TAG, "max id of mentions is"+maxId+ " tweets size is " +tweets.size());
                if(internetAlertDialogue.checkForInternet()) {
                    populateTimeline(sinceId, maxId, false);
                }
                sinceId = -1;
                maxId = -1;
            }
        });

        return view;
    }

    public void populateTimeline(long localSinceId, long localMaxId, boolean insertAtTop) {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        Log.d(TAG, "since id is "+sinceId);
        Log.d(TAG, "max id in mentions is"+maxId);
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //JSON HERE
                if (insertAtTop) {
                    int old_tweets_size = tweets.size();
                    tweets.addAll(0, Tweet.fromJSONArray(response));
                    int new_tweets_size = tweets.size();
                    tweetsRecyclerAdapter.notifyItemRangeInserted(0, (new_tweets_size - old_tweets_size));
                } else {
                    tweets.addAll(Tweet.fromJSONArray(response));
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
                if(statusCode == 200) {
                  alert_user("Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                }

                if (tweets.size() == 0) {
                    Toast.makeText(mContext, "Tweet fetch failed!. Loading old saved Tweets!", Toast.LENGTH_LONG).show();
                    //Loading from DB. compared size to make sure the data is only loaded once
                    OfflineTweetClass getOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                    getOfflineTweetClass.loadOfflineTweets();
                }
            }
        }, localSinceId, localMaxId);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }
}

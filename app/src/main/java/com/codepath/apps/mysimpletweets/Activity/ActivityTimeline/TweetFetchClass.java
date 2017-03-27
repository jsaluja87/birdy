package com.codepath.apps.mysimpletweets.Activity.ActivityTimeline;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jsaluja on 3/26/2017.
 */

public class TweetFetchClass {
    //Send an API
    private final static String TAG = "Tweet_fetch_class";
    Context mContext;

    public TweetFetchClass(Context context) {
        mContext = context;
    }

    public void populateTimeline(ArrayList<Tweet> tweets, TweetsRecyclerAdapter tweetsRecyclerAdapter, TwitterClient client, long sinceId, long maxId, boolean insertAtTop) {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //JSON HERE
                if(insertAtTop) {
                    int old_tweets_size = tweets.size();
                    tweets.addAll(0, Tweet.fromJSONArray(response));
                    int new_tweets_size = tweets.size();
                    tweetsRecyclerAdapter.notifyItemRangeInserted(0, (new_tweets_size-old_tweets_size));
                } else {
                    tweets.addAll(Tweet.fromJSONArray(response));
                    tweetsRecyclerAdapter.notifyItemRangeInserted(tweetsRecyclerAdapter.getItemCount(), tweets.size());
                }
                OfflineTweetClass saveOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                Log.d(TAG, "Before saving the tweets" + tweets.size());
                saveOfflineTweetClass.saveTweetsToDB();

                Log.d(TAG, "Response is "+response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Response failed");

                InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
                if(!internetAlertDialogue.isNetworkAvailable()) {
                    internetAlertDialogue.alert_user("Network Issue", "Please connect the device to an internet network!");
                } else {
                    if (!internetAlertDialogue.isOnline()) {
                        internetAlertDialogue.alert_user("Network Issue", "Device network does not have internet access!");
                    } else {
                        Log.d("DEBUG", "!!!Device has internet access!!!");
                        if (tweets.size() == 0) {
                            Toast.makeText(mContext, "Tweet fetch failed!. Loading old saved Tweets!", Toast.LENGTH_LONG).show();
                            //Loading from DB. compared size to make sure the data is only loaded once
                            OfflineTweetClass getOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                            getOfflineTweetClass.loadOfflineTweets();
                        }
                    }
                }

            }
        }, sinceId, maxId);
    }


}

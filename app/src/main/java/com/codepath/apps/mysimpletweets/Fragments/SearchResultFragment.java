package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.models.sqlitehelper.Organization_Table.screenName;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class SearchResultFragment extends TweetListFragment {
    //Could have extended tweetlistFragment
    private final static String TAG = "search Fragment";
    TwitterClient client;

    public static SearchResultFragment newInstance(String query) {
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchResultFragment.setArguments(args);
        return searchResultFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        client = TwitterApplication.getRestClient();

        //  FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        //  View view = binding.getRoot();

        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        client = TwitterApplication.getRestClient();
        if(internetAlertDialogue.checkForInternet()) {
            populateSearchResults(sinceId, maxId, false);
        }

        lvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                maxId = tweets.get(tweets.size() - 1).getUid() - 1;
                if(internetAlertDialogue.checkForInternet()) {
                    populateSearchResults(sinceId, maxId, false);
                }
                sinceId = -1;
                maxId = -1;
            }
        });
        return view;
    }

    public void populateSearchResults(long localSinceId, long localMaxId, boolean insertAtTop) {

        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        String query = getArguments().getString("query");
        if((query == "null") || (query == "")) {
            alert_user("Query not valid", "Please enter a valid query!!!");
        } else {

            client.getSearchTweetsResults(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        JSONArray json = response.getJSONArray("statuses");
                        if (insertAtTop) {
                            int old_tweets_size = tweets.size();
                            tweets.addAll(0, Tweet.fromJSONArray(json));
                            int new_tweets_size = tweets.size();
                            tweetsRecyclerAdapter.notifyItemRangeInserted(0, (new_tweets_size - old_tweets_size));
                        } else {
                            tweets.addAll(Tweet.fromJSONArray(json));
                            tweetsRecyclerAdapter.notifyItemRangeInserted(tweetsRecyclerAdapter.getItemCount(), tweets.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    OfflineTweetClass saveOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                    Log.d(TAG, "Before saving the tweets" + tweets.size());
                    saveOfflineTweetClass.saveTweetsToDB();

                    Log.d(TAG, "Response is " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, "Response failed");

                    if (statusCode == 200) {
                        alert_user("Home Timeline Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                    }

                    if (tweets.size() == 0) {
                        Toast.makeText(mContext, "Tweet fetch failed!. Loading old saved Tweets!", Toast.LENGTH_LONG).show();
                        //Loading from DB. compared size to make sure the data is only loaded once
                        OfflineTweetClass getOfflineTweetClass = new OfflineTweetClass(tweets, tweetsRecyclerAdapter);
                        getOfflineTweetClass.loadOfflineTweets();
                    }
                }
            }, query, localSinceId, localMaxId);
        }
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

}

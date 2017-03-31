package com.codepath.apps.mysimpletweets.Clients;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static android.R.attr.clearTaskOnLaunch;
import static android.R.attr.max;
import static android.content.ContentValues.TAG;
import static org.scribe.model.Verb.GET;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1"; //base API URL
	public static final String REST_CONSUMER_KEY = "6oTGwLUC4L1HDE41quPBOzCot";
	public static final String REST_CONSUMER_SECRET = "B0PAKQCW8TGLyi2pmPV9EPIDhjw4ZcG03e5weYbSV1lgPGgvmC";
    public static final String REST_CALLBACK_URL = "oauth://mysimpletweets"; // Change this (here and in manifest)

    final static String TAG = "TwitterClient";
    public static final int MAX_FETCHED_TWEETS = 5;
	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
//Create a method for the endpoint
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long sinceId,  long maxId) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", MAX_FETCHED_TWEETS);
        if(sinceId != -1) {params.put("since_id", sinceId);}
        if(maxId != -1) {params.put("max_id",maxId);
        }
        getClient().get(apiUrl, params, handler);
        Log.d(TAG, "URL is "+apiUrl);
    }

    //Posting statuses
    public void postStatuses(AsyncHttpResponseHandler handler, String status) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        Log.d(TAG, "Sending " +apiUrl);
        getClient().post(apiUrl, params, handler);
    }

    //Getting the current user's credentials
    public void getUserCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

    //Getting details of tweets the user was mentioned in
    //Create a method for the endpoint
    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long sinceId,  long maxId) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
        Log.d(TAG, "URL is "+apiUrl);
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", MAX_FETCHED_TWEETS);
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
        Log.d(TAG, "URL is "+apiUrl);
    }
}

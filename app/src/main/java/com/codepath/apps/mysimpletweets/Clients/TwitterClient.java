package com.codepath.apps.mysimpletweets.Clients;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1"; //base API URL
	public static final String REST_CONSUMER_KEY = "o7nFUlaqVrBHSdZn8DlFcv0Lp";
	public static final String REST_CONSUMER_SECRET = "WbrjWYlxmZyVqkrkd2IJkEOZtUYtItZZbqF0dPzjejSIR6uVx3";
    public static final String REST_CALLBACK_URL = "oauth://mysimpletweets"; // Change this (here and in manifest)

    final static String TAG = "TwitterClient";
    public static final int MAX_FETCHED_TWEETS = 5;
    public static final int MAX_FETCHED_FOLLOWERS = 20;
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
    public void getUserCredentials(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    //Getting details of tweets the user was mentioned in
    //Create a method for the endpoint
    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long sinceId,  long maxId) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if(sinceId != -1) {params.put("since_id", sinceId);}
        if(maxId != -1) {params.put("max_id",maxId);}
        getClient().get(apiUrl, params, handler);
        Log.d(TAG, "URL is "+apiUrl);
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName, long sinceId, long maxId) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", MAX_FETCHED_TWEETS);
        params.put("screen_name", screenName);
        if(sinceId != -1) {params.put("since_id", sinceId);}
        if(maxId != -1) {params.put("max_id",maxId);}
        getClient().get(apiUrl, params, handler);
        Log.d(TAG, "URL is "+apiUrl);
    }

    //Getting the clicked user's credentials
    public void getForeignUserProfile(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    //Mark or unmark a tweet as favorite
    public void setFavoriteOnTweet(JsonHttpResponseHandler handler, boolean favorite, long id){
        String apiUrl = getApiUrl("favorites/create.json");
        if(favorite) apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        getClient().post(apiUrl,params,handler);
    }

    //retweet
    public void retweet(AsyncHttpResponseHandler handler, boolean retweeted, long id){
        String apiUrl;
        if(retweeted) apiUrl = getApiUrl("statuses/unretweet/"+id+".json");
        else apiUrl = getApiUrl("statuses/retweet/"+id+".json");

        Log.d(TAG, "retweet apiURL is "+apiUrl);
        Log.d(TAG, "ID is "+id);
        getClient().post(apiUrl,handler);
    }

    //Get followers list
    public void getFollowersList(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        Log.d(TAG, "URL is "+apiUrl);
        params.put("count", MAX_FETCHED_FOLLOWERS);
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    //Get following list
    public void getFollowingList(AsyncHttpResponseHandler handler, String screenName) {
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }
    //Search tweet
    public void getSearchTweetsResults(AsyncHttpResponseHandler handler, String query, long sinceId, long maxId) {
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        params.put("q", query);
        if(sinceId != -1) {params.put("since_id", sinceId);}
        if(maxId != -1) {params.put("max_id",maxId);}
        getClient().get(apiUrl, params, handler);
    }
    //Send direct messages
    public void sendDirectMessage(AsyncHttpResponseHandler handler, String messageText, String screenName) {
        String apiUrl = getApiUrl("direct_messages/new.json");
        RequestParams params = new RequestParams();
        params.put("text", messageText);
        params.put("screen_name", screenName);
        getClient().post(apiUrl, params, handler);
    }
    //See recent direct messages
    public void getDirectMessages(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("direct_messages.json");
        getClient().get(apiUrl, null, handler);
    }

}

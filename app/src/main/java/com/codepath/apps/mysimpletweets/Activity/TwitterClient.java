package com.codepath.apps.mysimpletweets.Activity;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static org.scribe.model.Verb.GET;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1"; //base API URL
	public static final String REST_CONSUMER_KEY = "dDxKi9gLSsaUFiPmaJx8s2lK2";
	public static final String REST_CONSUMER_SECRET = "ypiLNX56jMZy9rRNu1wUfkX8l63vtjmzJtcGhEp97sIY6cEtcO";
    public static final String REST_CALLBACK_URL = "oauth://mysimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	//METHODS == ENDPOINT
	//Endpoints:
/*	- Get the home timeline from the user(Link https://dev.twitter.com/rest/reference/get/statuses/home_timeline)
            ○ GET statuses/home_timeline.json
            ○ We will get
            § Count=25
            § Since_id=1


*/
//Create a method for the endpoint
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        //sinceid =1 Show tweets since very first tweet.
        params.put("since_id", 1);
        getClient().get(apiUrl, params, handler);
        Log.d("DEBUG", "URL is "+apiUrl);
    }

    //COMPOSE TWEET METHOD
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 *
	 *
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}

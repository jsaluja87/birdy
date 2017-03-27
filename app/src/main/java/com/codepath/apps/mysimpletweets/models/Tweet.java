package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by jsaluja on 3/21/2017.
 */

//Parse the JSON +Store the data, encapsulate state logic or display logic
@Parcel
public class Tweet {
    // list out the attributes that we care about
    //parse the JSON
    public String body = null;
    public long uid = -1; //Uniquee DB id for tweet
    public String uidStr = null;
    public User user;
    public String createdAt = null;
    public Entities entities;

    public String relativeDate = null;

    public User getUser() {return user;}
    public String getBody() {
        if (body == null) {return "";}
        else {return body;}
    }
    public void setBody(String body) {this.body = body;}

    public long   getUid() {return uid;}

    public String getCreatedAt() {
        if(createdAt == null) {
            return "";
        }else {
            ParseRelativeTime p = new ParseRelativeTime();
            relativeDate = p.getRelativeTimeAgo(createdAt);
            return relativeDate;
        }
    }

    public Entities getEntities() {return entities;}

    public String getUidStr() {
        if (uidStr == null) {return "";}
        else {return uidStr;}
    }

    public void setUid(long uid) {this.uid = uid;}

    public void setUidStr(String uidStr) {this.uidStr = uidStr;}

    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public void setEntities(Entities entities) {this.entities = entities;}

    public void setUser(User user) {this.user = user;}

    //Deserialize the JSON and build tweet objects
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.uidStr = jsonObject.getString("id_str");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.entities = Entities.fromJSON(jsonObject.getJSONObject("entities"));
            Log.d("DEBUG","entities string is"+tweet.entities.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i< jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
            return tweets;
    }

}

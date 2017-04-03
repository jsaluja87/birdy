package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import static android.util.Log.d;
import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

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
    public boolean favorited = false;
    public int favoriteCount = -1;
    public boolean retweeted = false;

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

    public boolean getFavorited() {return favorited;}

    public void setFavorited(boolean favorited) {this.favorited = favorited;}

    public int getFavoriteCount() {return favoriteCount;}

    public void setFavoriteCount(int favoriteCount) {this.favoriteCount = favoriteCount;}

    public boolean getRetweeted() {return retweeted;}

    public void setRetweeted(boolean retweeted) {this.retweeted = retweeted;}

    //Deserialize the JSON and build tweet objects
    public static Tweet fromJSON(JSONObject jsonObject) {

        Tweet tweet = new Tweet();
        try {
            if(jsonObject.has("text")) tweet.body = jsonObject.getString("text");
            if(jsonObject.has("id")) tweet.uid = jsonObject.getLong("id");
            if(jsonObject.has("id_str")) tweet.uidStr = jsonObject.getString("id_str");
            if(jsonObject.has("created_at")) tweet.createdAt = jsonObject.getString("created_at");
            if(jsonObject.has("user")) tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            if(jsonObject.has("entities")) tweet.entities = Entities.fromJSON(jsonObject.getJSONObject("entities"));
            if(jsonObject.has("favourites_count")) tweet.favoriteCount = jsonObject.getInt("favourites_count");
            if(jsonObject.has("favorited")) d("Printing_user", "favorited is "+jsonObject.getBoolean("favorited"));
            if(jsonObject.has("favorited")) tweet.favorited = jsonObject.getBoolean("favorited");
            if(jsonObject.has("retweeted")) tweet.retweeted = jsonObject.getBoolean("retweeted");
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

package com.codepath.apps.mysimpletweets.models;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by jsaluja on 3/22/2017.
 */

@Parcel
public class User implements Parcelable {
    public String name;
    public long uid = -1;
    public String screenName = null;
    public String profileNameUrl = null;
    public String profileNameUrlHttps = null;
    public String tagline;
    public int followersCount;
    public int friendsCount;

    public String getName() {
        if(name == null) return "";
        else return name;}

    public long getUid() {return uid;}

    public String getScreenName() {
        if(screenName == null) return "";
        else return screenName;}

    public String getProfileNameUrl() {
        if(profileNameUrl == null) return "";
        else return profileNameUrl;}

    public String getProfileNameUrlHttps() {
        if(profileNameUrlHttps == null) return "";
        else return profileNameUrl;}

    public void setName(String name) {this.name = name;}

    public void setUid(long uid) {this.uid = uid;}

    public void setScreenName(String screenName) {this.screenName = screenName;}

    public void setProfileNameUrl(String profileNameUrl) {this.profileNameUrl = profileNameUrl;}

    public void setProfileNameUrlHttps(String profileNameUrlHttps) {this.profileNameUrlHttps = profileNameUrlHttps;}

    public String getTagline() {return tagline;}

    public void setTagline(String tagline) {this.tagline = tagline;}

    public int getFollowersCount() {return followersCount;}

    public void setFollowersCount(int followersCount) {this.followersCount = followersCount;}

    public int getFollowingCount() {return friendsCount;}

    public void setFollowingCount(int followingCount) {this.friendsCount = followingCount;}

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        //FIXME - extract only when present
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileNameUrl = jsonObject.getString("profile_image_url");
            user.profileNameUrlHttps = jsonObject.getString("profile_image_url_https");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.friendsCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<User> fromJSONArray(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();

        for(int i = 0; i< jsonArray.length(); i++) {
            JSONObject userJson = null;
            try {
                userJson = jsonArray.getJSONObject(i);
                User user = User.fromJSON(userJson);
                if(user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {

    }
    public static final Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(android.os.Parcel source) {
            return null;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
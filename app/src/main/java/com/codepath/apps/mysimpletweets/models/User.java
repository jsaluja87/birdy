package com.codepath.apps.mysimpletweets.models;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

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

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        //FIXME - extract only when present
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileNameUrl = jsonObject.getString("profile_image_url");
            user.profileNameUrlHttps = jsonObject.getString("profile_image_url_https");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
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
package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class Sender {
    private String name;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static Sender fromJSON(JSONObject jsonObject) {
        Sender sender = new Sender();
        //FIXME - extract only when present
        try {
            sender.name = jsonObject.getString("name");
            sender.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sender;
    }

}

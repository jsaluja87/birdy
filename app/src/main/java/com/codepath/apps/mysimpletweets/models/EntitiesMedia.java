package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jsaluja on 3/25/2017.
 */

@Parcel
public class EntitiesMedia {
    public String expandedUrl;
    public String type = "null";
    public String mediaUrl;

    public String getMediaUrl() {
        if(expandedUrl == null) return "";
        else return mediaUrl;}

    public String getType() {
        if(type == "null")return "";
        else return type;}

    public String getExpandedUrl() {
        if(expandedUrl == null) return "";
        else return expandedUrl;
    }

    public static EntitiesMedia fromJSON(JSONArray jsonArray) {
        EntitiesMedia entitiesMedia = new EntitiesMedia();
        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            entitiesMedia.expandedUrl = jsonObject.getString("expanded_url");
            entitiesMedia.type = jsonObject.getString("type");
            entitiesMedia.mediaUrl = jsonObject.getString("media_url");
            Log.d("DEBUG", "jsonObject is(from media object)" + jsonObject);
            Log.d("DEBUG", "jsonObject URL is(from media object)" + jsonObject.getString("expanded_url"));
            Log.d("DEBUG", "Embedded image URL is(from media object)" + entitiesMedia.expandedUrl);
            Log.d("DEBUG", "Embedded image URL is(from media object)" + entitiesMedia.getExpandedUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entitiesMedia;
    }
}

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
public class ExtendedEntitiesMedia {
    public String expandedUrl;
    public String getexpandedUrl() {
        if(expandedUrl == null) return "";
        else return expandedUrl;}

    public static ExtendedEntitiesMedia fromJSON(JSONArray jsonArray) {
        ExtendedEntitiesMedia extendedEntitiesMedia = new ExtendedEntitiesMedia();
        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            extendedEntitiesMedia.expandedUrl = jsonObject.getString("expanded_url");
            Log.d("DEBUG", "Embedded image URL is(from extended entities media)" + extendedEntitiesMedia.getexpandedUrl().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extendedEntitiesMedia;
    }
}

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
public class ExtendedEntities {
    public ExtendedEntitiesMedia media;
    public ExtendedEntitiesMedia getMedia() { return media;}

    public static ExtendedEntities fromJSON(JSONObject jsonObject) {
        ExtendedEntities extendedEntities = new ExtendedEntities();
        //FIXME - extract only when present
        try {

            extendedEntities.media = ExtendedEntitiesMedia.fromJSON(jsonObject.getJSONArray("media"));
            Log.d("DEBUG", "Returning media array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extendedEntities;
    }
}

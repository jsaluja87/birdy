package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jsaluja on 3/23/2017.
 */

@Parcel
public class Entities {
    public Urls urls;
    public ExtendedEntities extendedEntities;
    public EntitiesMedia entitiesMedia;

    public Urls getUrls() {return urls;}
    public ExtendedEntities getExtendedEntities() {return extendedEntities;}
    public EntitiesMedia getEntitiesMedia() {return entitiesMedia;}

    public static Entities fromJSON(JSONObject jsonObject) {
        Entities entities = new Entities();
        //FIXME - extract only when present
        try {
        //    JSONArray jsonArray = jsonObject.getJSONArray("urls");
            entities.urls = Urls.fromJSON(jsonObject.getJSONArray("urls"));
            entities.entitiesMedia = EntitiesMedia.fromJSON((jsonObject.getJSONArray("media")));
            entities.extendedEntities = ExtendedEntities.fromJSON(jsonObject.getJSONObject("extended_entities"));
            Log.d("DEBUG", "Embedded urls is "+entities.urls.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
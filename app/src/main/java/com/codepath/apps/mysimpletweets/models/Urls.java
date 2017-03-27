package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import static android.media.CamcorderProfile.get;

/**
 * Created by jsaluja on 3/23/2017.
 */
@Parcel
public class Urls {
    public String url;
    public String expanded_url;
    public String display_url;

    public String getUrl() {return url;}
    public String getExpandedUrl() {return expanded_url;}
    public String getDisplayUrl() {return display_url;}

    public static Urls fromJSON(JSONArray jsonArray) {
        Urls urls = new Urls();
        //FIXME - extract only when present
        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            urls.url = jsonObject.getString("url");
            urls.display_url = jsonObject.getString("display_url");
            urls.expanded_url = jsonObject.getString("expanded_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return urls;
    }

}

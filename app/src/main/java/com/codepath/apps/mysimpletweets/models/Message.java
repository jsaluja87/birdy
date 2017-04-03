package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class Message {
    private String senderScreenName;
    private String text;
    private String createdAt;
    private Entities entities;
    private Sender sender;
    private final static String TAG = "Message class";

    public String relativeDate = null;

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public void setSenderScreenName(String senderScreenName) {
        this.senderScreenName = senderScreenName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        if(createdAt == null) {
            return "";
        }else {
            ParseRelativeTime p = new ParseRelativeTime();
            relativeDate = p.getRelativeTimeAgo(createdAt);
            return relativeDate;
        }
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    //Deserialize the JSON and build tweet objects
    public static Message fromJSON(JSONObject jsonObject) {

        Message message = new Message();
        try {
            if(jsonObject.has("text")) message.text = jsonObject.getString("text");
            if(jsonObject.has("sender_screen_name")) message.senderScreenName = jsonObject.getString("sender_screen_name");
            if(jsonObject.has("created_at")) message.createdAt = jsonObject.getString("created_at");
            if(jsonObject.has("sender")) message.sender = Sender.fromJSON(jsonObject.getJSONObject("sender"));
            if(jsonObject.has("entities")) message.entities = Entities.fromJSON(jsonObject.getJSONObject("entities"));
            Log.d(TAG, "JSONObject is " +jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
    public static ArrayList<Message> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Message> messages = new ArrayList<>();

        for(int i = 0; i< jsonArray.length(); i++) {
            JSONObject messageJson = null;
            try {
                messageJson = jsonArray.getJSONObject(i);
                Message message = Message.fromJSON(messageJson);
                if(message != null) {
                    messages.add(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return messages;
    }

}

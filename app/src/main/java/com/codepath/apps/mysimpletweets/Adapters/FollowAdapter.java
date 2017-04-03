package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.Activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class FollowAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<User> mUsers;
    private Context mContext;

    public FollowAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View ViewTweets = inflater.inflate(R.layout.tweets_item, parent, false);
        viewHolder = new ViewHolderUserFollowItems(ViewTweets);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderUserFollowItems vhItem = (ViewHolderUserFollowItems) holder;
        final User user = mUsers.get(position);

        if (user != null) {
            Glide.with(mContext)
                    .load(user.getProfileNameUrl())
                    .into(vhItem.followUserProfileImage);
            vhItem.followUserName.setText(user.getName());
            vhItem.followUserScreenName.setText(user.getScreenName());

        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderUserFollowItems vhImage = (ViewHolderUserFollowItems) holder;
        Glide.clear(vhImage.followUserProfileImage);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
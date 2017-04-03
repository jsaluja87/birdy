package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.mysimpletweets.Activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.Fragments.NewDirectMessageFragment;
import com.codepath.apps.mysimpletweets.Fragments.TweetComposeFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.PatternEditableBuilder;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Message> mMessages;


    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View ViewMessages = inflater.inflate(R.layout.tweets_item, parent, false);
        viewHolder = new ViewHolderMessageItems(ViewMessages);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolderMessageItems vhItem = (ViewHolderMessageItems) viewHolder;
        configureViewHolderMessageItem(vhItem, position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderMessageItems vhImage = (ViewHolderMessageItems) holder;
        Glide.clear(vhImage.messageEmbeddedPicUrl);
        Glide.clear(vhImage.messageProfileImageUrl);
    }

    private void configureViewHolderMessageItem(ViewHolderMessageItems holder, int position) {
        Message message = mMessages.get(position);

        if(message.getSender() != null) {
            if (!TextUtils.isEmpty(message.getSender().getProfileImageUrl())) {
                Glide.with(mContext).load(message.getSender().getProfileImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.messageProfileImageUrl);
            }
            holder.senderName.setText(message.getSender().getName());
        }

        holder.senderScreenName.setText(message.getSenderScreenName());
        holder.messageText.setText(message.getText());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        text -> Toast.makeText(mContext, "Clicked username: " + text,
                                Toast.LENGTH_SHORT).show()).into(holder.messageText);

        holder.messageCreatedAt.setText(message.getCreatedAt());

        Glide.with(mContext).load("").override(1,1).into(holder.messageEmbeddedPicUrl);
        if(message.getEntities() != null) {
            if ( message.getEntities().getEntitiesMedia() != null) {
                if(!TextUtils.isEmpty(message.getEntities().getEntitiesMedia().getMediaUrl())) {
                    if(message.getEntities().getEntitiesMedia().getType().equals("photo")) {
                        Glide.with(mContext).load(message.getEntities().getEntitiesMedia().getMediaUrl())
                             .diskCacheStrategy(DiskCacheStrategy.ALL).override(1000,300).into(holder.messageEmbeddedPicUrl);
                    }
                }
            }
        }

        holder.messageProfileImageUrl.setOnClickListener(v -> {
            Intent userProfile = new Intent(mContext, ProfileActivity.class);
            //        userProfile.putExtra("user", mTweets.get(0).getUser());
            userProfile.putExtra("screen_name", mMessages.get(position).getSenderScreenName());
            mContext.startActivity(userProfile);
        });

        holder.tweetReplyButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("screen_name",message.getSenderScreenName());

            FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
            NewDirectMessageFragment newDirectMessageFragment = new NewDirectMessageFragment();
            newDirectMessageFragment.setArguments(bundle);
            newDirectMessageFragment.show(fm, "fragment_new_message");
        });

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

}
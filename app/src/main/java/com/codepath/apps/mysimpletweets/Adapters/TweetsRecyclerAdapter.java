package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.apps.mysimpletweets.Activity.ActivityDetail;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.ItemClickSupport;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.TimelineActivity;
import com.codepath.apps.mysimpletweets.Activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.Fragments.TweetComposeFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.PatternEditableBuilder;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.R.attr.bitmap;
import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/**
 * Created by jsaluja on 3/22/2017.
 */

public class TweetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Tweet> mTweets;


    public TweetsRecyclerAdapter(Context context, ArrayList<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View ViewTweets = inflater.inflate(R.layout.tweets_item, parent, false);
        viewHolder = new ViewHolderTweetsItem(ViewTweets);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolderTweetsItem vhItem = (ViewHolderTweetsItem) viewHolder;
        configureViewHolderTweetsItem(vhItem, position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderTweetsItem vhImage = (ViewHolderTweetsItem) holder;
        Glide.clear(vhImage.tweetProfilePicUrl);
        Glide.clear(vhImage.tweetEmbeddedPicUrl);
    }

    private void configureViewHolderTweetsItem(ViewHolderTweetsItem holder, int position) {
        Tweet tweet = mTweets.get(position);

        if(tweet.getUser() != null) {
            if (!TextUtils.isEmpty(tweet.getUser().getProfileNameUrl())) {
                Glide.with(mContext).load(tweet.getUser().getProfileNameUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.tweetProfilePicUrl);
            }
            holder.tweetUser.setText(tweet.getUser().getName());
            holder.tweetUserName.setText("@"+tweet.getUser().getScreenName());
        }

        holder.tweetBody.setText(tweet.getBody());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        text -> Toast.makeText(mContext, "Clicked username: " + text,
                                Toast.LENGTH_SHORT).show()).into(holder.tweetBody);

        holder.tweetCreatedAt.setText(tweet.getCreatedAt());

        Glide.with(mContext).load("").override(1,1).into(holder.tweetEmbeddedPicUrl);
        if(tweet.getEntities() != null) {
            if (tweet.getEntities().getEntitiesMedia() != null) {
                if(!TextUtils.isEmpty(tweet.getEntities().getEntitiesMedia().getMediaUrl())) {
                    if(tweet.getEntities().getEntitiesMedia().getType().equals("photo")) {
                        Glide.with(mContext).load(tweet.getEntities().getEntitiesMedia().getMediaUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(1000,300).into(holder.tweetEmbeddedPicUrl);
                    }
                }
            }
        }

        holder.tweetProfilePicUrl.setOnClickListener(v -> {
            Intent userProfile = new Intent(mContext, ProfileActivity.class);
            //        userProfile.putExtra("user", mTweets.get(0).getUser());
            userProfile.putExtra("screen_name", mTweets.get(position).getUser().getScreenName());
            mContext.startActivity(userProfile);
        });

        holder.tweetReplyButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", mTweets.get(position).getUser());
            bundle.putInt("reply_tweet", 1);

            FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
            TweetComposeFragment filterFragmentObject = TweetComposeFragment.newInstance();
            filterFragmentObject.setArguments(bundle);
            filterFragmentObject.show(fm, "fragment_edit_name");
        });

    }

        @Override
        public int getItemCount() {
            return mTweets.size();
        }

}
package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
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
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;

import static android.R.attr.bitmap;

/**
 * Created by jsaluja on 3/22/2017.
 */

public class TweetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

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
                holder.tweetUserName.setText(tweet.getUser().getScreenName());
            }
            holder.tweetBody.setText(tweet.getBody());
            holder.tweetCreatedAt.setText(tweet.getCreatedAt());

            //FIXME - Imageview changes position on changing widht/height

            //FIXME - get a good twitterific place holders for all offline tweets
         //   holder.tweetEmbeddedPicUrl.setLayoutParams(new RelativeLayout.LayoutParams(1,1));
           // To remove extra space by imageview
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
        }

        @Override
        public int getItemCount() {
            return mTweets.size();
        }

}
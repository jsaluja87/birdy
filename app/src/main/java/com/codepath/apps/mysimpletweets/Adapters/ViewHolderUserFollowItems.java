package com.codepath.apps.mysimpletweets.Adapters;

import android.graphics.drawable.Icon;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by jsaluja on 4/1/2017.
 */

class ViewHolderUserFollowItems extends RecyclerView.ViewHolder {

    TextView followUserName;
    TextView followUserDescription;
    ImageView followUserProfileImage;
    TextView followUserScreenName;
    ImageView followUserIcon;



    ViewHolderUserFollowItems(View view) {
        super(view);
        followUserName = (TextView)itemView.findViewById(R.id.TweetsNameId);
        followUserProfileImage = (RoundedImageView)itemView.findViewById(R.id.TweetsImageId);
        followUserScreenName = (TextView)itemView.findViewById(R.id.TweetsUserNameId);

    }
}
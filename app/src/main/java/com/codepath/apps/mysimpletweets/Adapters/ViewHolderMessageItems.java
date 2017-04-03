package com.codepath.apps.mysimpletweets.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.LinkifiedTextView;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class ViewHolderMessageItems extends RecyclerView.ViewHolder {
    public com.makeramen.roundedimageview.RoundedImageView messageProfileImageUrl;
    public TextView messageText;
    public TextView senderName;
    public TextView senderScreenName;
    public TextView messageCreatedAt;
    public com.makeramen.roundedimageview.RoundedImageView messageEmbeddedPicUrl;
    public ImageButton tweetReplyButton;


    public ViewHolderMessageItems(View itemView) {
        super(itemView);
        messageProfileImageUrl = (com.makeramen.roundedimageview.RoundedImageView)itemView.findViewById(R.id.TweetsImageId);
        senderName = (TextView)itemView.findViewById(R.id.TweetsNameId);
        senderScreenName = (TextView)itemView.findViewById(R.id.TweetsUserNameId);
        messageText = (LinkifiedTextView)itemView.findViewById(R.id.TweetsBodyid);
        messageCreatedAt = (TextView)itemView.findViewById(R.id.TweetsCreatedAtId);
        messageEmbeddedPicUrl = (com.makeramen.roundedimageview.RoundedImageView)itemView.findViewById(R.id.TweetsEmbeddedImageId);
        tweetReplyButton = (ImageButton)itemView.findViewById(R.id.TweetsReplyButtonImageId);

    }
}


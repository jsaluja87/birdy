package com.codepath.apps.mysimpletweets.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.LinkifiedTextView;


/**
 * Created by jsaluja on 3/22/2017.
 */

public class ViewHolderTweetsItem extends RecyclerView.ViewHolder {
    public com.makeramen.roundedimageview.RoundedImageView tweetProfilePicUrl;
    public TextView tweetUser;
    public TextView tweetUserName;
    public LinkifiedTextView tweetBody;
    public TextView tweetCreatedAt;
    public com.makeramen.roundedimageview.RoundedImageView tweetEmbeddedPicUrl;


    public LinkifiedTextView getTweetBody() {return tweetBody;}

    public TextView getTweetCreatedAt() {return tweetCreatedAt;}

    public ViewHolderTweetsItem(View itemView) {
        super(itemView);
        tweetProfilePicUrl = (com.makeramen.roundedimageview.RoundedImageView)itemView.findViewById(R.id.TweetsImageId);
        tweetUser = (TextView)itemView.findViewById(R.id.TweetsNameId);
        tweetUserName = (TextView)itemView.findViewById(R.id.TweetsUserNameId);
        tweetBody = (LinkifiedTextView)itemView.findViewById(R.id.TweetsBodyid);
        tweetCreatedAt = (TextView)itemView.findViewById(R.id.TweetsCreatedAtId);
        tweetEmbeddedPicUrl = (com.makeramen.roundedimageview.RoundedImageView)itemView.findViewById(R.id.TweetsEmbeddedImageId);

    }
}

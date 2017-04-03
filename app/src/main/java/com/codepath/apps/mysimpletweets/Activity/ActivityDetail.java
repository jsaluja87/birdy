package com.codepath.apps.mysimpletweets.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
//import com.codepath.apps.mysimpletweets.databinding.ActivityDetailBinding;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.databinding.ActivityDetailBinding;
import com.codepath.apps.mysimpletweets.SupportingClasses.LinkifiedTextView;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;

public class ActivityDetail extends AppCompatActivity {
    private com.makeramen.roundedimageview.RoundedImageView userProfileImage;
    private TextView userProfileName;
    private TextView userDisplayName;
    private Button tweetDoneButton;
    private LinkifiedTextView tweetBody;
    private com.makeramen.roundedimageview.RoundedImageView tweetMedia;
    private TextView tweetCreatedAt;
    private EditText tweetReplyText;
    private Button tweetSaveButton;
    private ImageButton tweetReplyButton;
    private ImageButton tweetRetweetButton;
    private ImageButton tweetFavoriteButton;
    private final static String TAG ="Detail_Activity_Log";
    private TwitterClient client;
    private TextInputLayout tweetReplyWrapper;
    private InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(ActivityDetail.this);
    private Tweet tweet;
    private int  listPositionClicked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        dataBindFragmentValues(binding);

     //   User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user_object"));
        listPositionClicked = (int)getIntent().getIntExtra("position", 0);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet_object"));
        String userName = getIntent().getStringExtra("user_name");
        String userScreenName = getIntent().getStringExtra("user_screen_name");
        String userPicUrl = getIntent().getStringExtra("user_pic_url");

        if (!TextUtils.isEmpty(userPicUrl)) {
            Glide.with(ActivityDetail.this).load(userPicUrl).into(userProfileImage);
        }
        if(userName != null) userProfileName.setText(userName);
        if(userScreenName != null) userDisplayName.setText("@" + userScreenName);

        Log.d(TAG, "tweet favorited while entering "+tweet.getFavorited());
        if(tweet.getFavorited()) tweetFavoriteButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_heart_red));
        else  tweetFavoriteButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_heart));

        if(tweet.getRetweeted())tweetRetweetButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_retweet_green));
        else tweetRetweetButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_retweet));

        if(userScreenName != null) tweetReplyText.setText("@" + userScreenName);
        tweetReplyText.setSelection(tweetReplyText.getText().length());

        if(tweet.getBody() != null) tweetBody.setText(tweet.getBody());
        //MEDIA
        if(tweet.getCreatedAt() != null) tweetCreatedAt.setText(tweet.getCreatedAt());

        tweetReplyText.setVisibility(View.GONE);
        tweetSaveButton.setVisibility(View.GONE);
        tweetReplyWrapper.setVisibility(View.GONE);

        if(tweet.getEntities() != null) {
            if (tweet.getEntities().getEntitiesMedia() != null) {
                if(!TextUtils.isEmpty(tweet.getEntities().getEntitiesMedia().getMediaUrl())) {
                    if(tweet.getEntities().getEntitiesMedia().getType().equals("photo")) {
                        String Url = tweet.getEntities().getEntitiesMedia().getMediaUrl();
                        Log.d("Debug","From adapter tweet String URL "+Url);
                        Glide.with(ActivityDetail.this).load(tweet.getEntities().getEntitiesMedia().getMediaUrl()).into(tweetMedia);
                    }
                }
            }
        }

        tweetReplyButton.setOnClickListener(v -> {
            tweetReplyText.setVisibility(View.VISIBLE);
            tweetSaveButton.setVisibility(View.VISIBLE);
            tweetReplyWrapper.setVisibility(View.VISIBLE);
        });

        tweetDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFinish(tweet.getFavorited(), tweet.getRetweeted());
            }
        });

        tweetSaveButton.setOnClickListener(v -> {
            if(internetAlertDialogue.checkForInternet()) {
                client = TwitterApplication.getRestClient();
                client.postStatuses(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(TAG, "TWEET REPLY POST SUCCESS");
                        callFinish(tweet.getFavorited(), tweet.getRetweeted());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, "Post failed");

                        if(statusCode == 200) {
                            alert_user("Reply post failed!", "The request sent out is good. Response failed from the website!!!");
                        }

                    }
                }, tweetReplyText.getText().toString());
            }

        });

        tweetFavoriteButton.setOnClickListener(v -> {
            if (internetAlertDialogue.checkForInternet()) {

                Log.d(TAG, "Did the tweet get favorited outside "+tweet.getFavorited());
                client = TwitterApplication.getRestClient();
                client.setFavoriteOnTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            tweet = Tweet.fromJSON(response);
                            Log.d(TAG, "Response is"+response);
                            if (tweet.getFavorited()) tweetFavoriteButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_heart_red));
                            else  tweetFavoriteButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_heart));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d(TAG, "Tweet Favorite response failed, statusCode "+statusCode);
                    }
                }, tweet.getFavorited(), tweet.getUid());
            }
        });

        tweetRetweetButton.setOnClickListener(v -> {
            if (internetAlertDialogue.checkForInternet()) {
                client = TwitterApplication.getRestClient();
                client.retweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            tweet = Tweet.fromJSON(response);
                            if (tweet.getRetweeted()) tweetRetweetButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_retweet_green));
                            else tweetRetweetButton.setImageDrawable(ContextCompat.getDrawable(ActivityDetail.this, R.drawable.ic_retweet));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d(TAG, "Tweet Retweet response failed, statusCode "+statusCode);
                    }

                }, tweet.getRetweeted(), tweet.getUid());
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Please use the done button to exit")
                .setPositiveButton("ok", null)
                .show();
    }


    public void callFinish(boolean favorited, boolean retweeted) {
        Intent callBack = new Intent();
        callBack.putExtra("wasFavorited", favorited);
        callBack.putExtra("wasRetweeted", retweeted);
        callBack.putExtra("position", listPositionClicked);
        setResult(RESULT_OK, callBack);
        finish();
    }
    public void alert_user(String title, String message) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(ActivityDetail.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setNegativeButton("Ok",
                (dialog1, which) -> dialog1.cancel());
        AlertDialog alertD = dialog.create();
        alertD.show();
    }
    public void dataBindFragmentValues(ActivityDetailBinding binding) {
        userProfileImage = binding.DetailUserPicid;
        userProfileName = binding.DetailUserNameId;
        userDisplayName = binding.DetailUserTagNameId;
        tweetDoneButton = binding.DetailDoneButtonId;
        tweetBody = binding.DetailTextBodyId;
        tweetMedia = binding.DetailMediaId;
        tweetCreatedAt = binding.DetailRelativeTimeId;
        tweetReplyText = binding.DetailEditTextId;
        tweetReplyButton = binding.ReplyButtonImageId;
        tweetRetweetButton = binding.RetweetButtonImageId;
        tweetFavoriteButton = binding.FavoriteButtonImageId;
        tweetReplyWrapper = binding.DetailEditTextReplyWrapperId;
        tweetReplyText = binding.DetailEditTextId;
        tweetSaveButton = binding.ReplyButtonId;
    }
}

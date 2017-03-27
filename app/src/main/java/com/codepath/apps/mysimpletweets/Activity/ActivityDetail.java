package com.codepath.apps.mysimpletweets.Activity;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ActivityDetail extends AppCompatActivity {
    private com.makeramen.roundedimageview.RoundedImageView userProfileImage;
    private TextView userProfileName;
    private TextView userDisplayName;
    private LinkifiedTextView tweetBody;
    private com.makeramen.roundedimageview.RoundedImageView tweetMedia;
    private TextView tweetCreatedAt;
    private EditText tweetReplyText;
    private ImageButton tweetSaveButton;
    private ImageButton tweetReplyButton;
    private final static String TAG ="Detail_Activity_Log";
    private TwitterClient client;
    private TextInputLayout tweetReplyWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        dataBindFragmentValues(binding);

     //   User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user_object"));
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet_object"));
        String userName = getIntent().getStringExtra("user_name");
        String userScreenName = getIntent().getStringExtra("user_screen_name");
        String userPicUrl = getIntent().getStringExtra("user_pic_url");

        if (!TextUtils.isEmpty(userPicUrl)) {
            Glide.with(ActivityDetail.this).load(userPicUrl).into(userProfileImage);
        }
        if(userName != null) userProfileName.setText(userName);
        if(userScreenName != null) userDisplayName.setText("@" + userScreenName);

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

        tweetSaveButton.setOnClickListener(v -> {

            client = TwitterApplication.getRestClient();
            client.postStatuses(new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, "TWEET REPLY POST SUCCESS");
                    finish();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    //FIXME - Check for internet and network
                    Log.d(TAG, "TWEET POST FAILED");
                    InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(ActivityDetail.this);
                    if(!internetAlertDialogue.isNetworkAvailable()) {
                        internetAlertDialogue.alert_user("Network Issue", "Please connect the device to an internet network!");
                    } else {
                        if (!internetAlertDialogue.isOnline()) {
                            internetAlertDialogue.alert_user("Network Issue", "Device network does not have internet access!");
                        } else {
                            Log.d("DEBUG", "!!!Device has internet access!!!");
                        }
                    }
                }
            }, tweetReplyText.getText().toString());

        });
    }

    public void dataBindFragmentValues(ActivityDetailBinding binding) {
        userProfileImage = binding.DetailUserPicid;
        userProfileName = binding.DetailUserNameId;
        userDisplayName = binding.DetailUserTagNameId;
        tweetBody = binding.DetailTextBodyId;
        tweetMedia = binding.DetailMediaId;
        tweetCreatedAt = binding.DetailRelativeTimeId;
        tweetReplyText = binding.DetailEditTextId;
        tweetReplyButton = binding.ReplyButtonImageId;
        tweetReplyWrapper = binding.DetailEditTextReplyWrapperId;
        tweetReplyText = binding.DetailEditTextId;
        tweetSaveButton = binding.ReplyButtonId;
    }
}

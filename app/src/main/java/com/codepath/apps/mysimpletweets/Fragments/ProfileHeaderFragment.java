package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.extras.PRNGFixes;

/**
 * Created by jsaluja on 3/30/2017.
 */

public class ProfileHeaderFragment extends Fragment {

    TwitterClient client;
    User user;
    View mView;
    Context mContext;
    private final static String TAG = "Profile Header Fragment";

    public static ProfileHeaderFragment newInstance(String screenName) {
        ProfileHeaderFragment profileHeaderFragment = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        if(screenName == "null") screenName = "jsaluja87";
        args.putString("screen_name", screenName);
        profileHeaderFragment.setArguments(args);
        return profileHeaderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        mView = view;
        mContext = view.getContext();
        // Data binding

      //  FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
      //  View view = binding.getRoot();


        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        client = TwitterApplication.getRestClient();
        if(internetAlertDialogue.checkForInternet()) {
            populateHeader();
        }

        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Repopulating on swiping
    }

    public void populateHeader() {
        String screenName = getArguments().getString("screen_name");

        if(screenName == "null") {
            client.getUserCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (statusCode == 200) {
                        alert_user("Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                    }
                }
            }, screenName);

        } else {
            client.getForeignUserProfile(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    alert_user("Result fetch failed!", "Please check your request!!");
                }
            }, screenName);
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) mView.findViewById(R.id.ProfileNameId);
        TextView tvTagLine = (TextView)mView.findViewById(R.id.ProfileTagId);
        TextView tvFollowers = (TextView)mView.findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)mView.findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) mView.findViewById(R.id.ProfileImageId);
        Log.d(TAG, "USER details are"+user.getName()+"\n"+user+"\n"+user.getProfileNameUrl());
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Glide.with(this).load(user.getProfileNameUrl()).fitCenter().into(ivProfileImage);
    }

    public void alert_user(String title, String message) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setNegativeButton("Ok",
                (dialog1, which) -> dialog1.cancel());
        AlertDialog alertD = dialog.create();
        alertD.show();
    }
}

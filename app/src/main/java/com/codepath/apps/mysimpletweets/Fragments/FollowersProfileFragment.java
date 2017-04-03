package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.Adapters.FollowAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class FollowersProfileFragment extends FollowProfileFragment {
    private TwitterClient client;
    private final static String TAG = "Followers fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static FollowersProfileFragment newInstance(String screenName, boolean insertAtTop) {
        FollowersProfileFragment followerFragment = new FollowersProfileFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        followerFragment.setArguments(args);
        return followerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);

        client = TwitterApplication.getRestClient();
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet()) {
            populateFollowers();
        }

        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(internetAlertDialogue.checkForInternet()) {
                    populateFollowers();
                }
            }
        });

        return view;
    }

    public void populateFollowers() {

        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        String screenName = getArguments().getString("screen_name");
        boolean insertAtTop = getArguments().getBoolean("insert_at_top");
        Log.d(TAG,"screen name is "+screenName);
        client.getFollowersList(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("users");

                    if (insertAtTop) {
                        int old_users_size = mUsers.size();
                        mUsers.addAll(0, User.fromJSONArray(jsonArray));
                        int new_users_size = mUsers.size();
                        followAdapter.notifyItemRangeInserted(0, (new_users_size - old_users_size));
                    } else {
                        mUsers.addAll(User.fromJSONArray(jsonArray));
                        followAdapter.notifyItemRangeInserted(followAdapter.getItemCount(), mUsers.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Response failed; Status code"+statusCode);
                if(statusCode == 200) {
                    alert_user("Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                }
            }
        }, screenName);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
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
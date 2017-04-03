package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.Activity.ActivityDetail;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.ItemClickSupport;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.TimelineActivity;
import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.RecyclerItemDecorator;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jsaluja on 3/28/2017.
 */

public class TweetListFragment extends Fragment {

    //FIXME - read all hints
    final static int NUM_GRID_COLUMNS = 1;
    final static String TAG = "TIMELINE_FRAGMENT_LOG";
    public ArrayList<Tweet> tweets;
    public TweetsRecyclerAdapter tweetsRecyclerAdapter;
    public RecyclerView lvTweets;
    public StaggeredGridLayoutManager gridLayoutManager;
    public EndlessRecyclerViewScrollListener scrollListener;
    public Context mContext;

    public void setSinceId(long sinceId) {this.sinceId = sinceId;}

    public void setMaxId(long maxId) {this.maxId = maxId;}

    public long sinceId = -1, maxId = -1;
    private boolean alreadyRefreshed = false;
   // private Toolbar toolbar;
    FloatingActionButton fab;
    User user_info_for_compose = null;
    public ProgressBar progressBar;
    public SmoothProgressBar smoothProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        mContext = view.getContext();
        //Implementing the smooth progress bar. The loading is so fast that the bar never shows up
        progressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity()).interpolator(new AccelerateInterpolator()).build());
        smoothProgressBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) smoothProgressBar.getIndeterminateDrawable()).getStrokeWidth()));

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        setupRecycleAdapter();

        //Displaying activity details. Using parcel
        ItemClickSupport.addTo(lvTweets).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent tweetDetails = new Intent(getActivity(), ActivityDetail.class);
                    if(tweets.get(position).getUser() != null) {
                        String name = tweets.get(position).getUser().getName();
                        Log.d(TAG, "USERRR is " + tweets.get(position).getUser().getProfileNameUrl());
                        tweetDetails.putExtra("user_name", tweets.get(position).getUser().getName());
                        tweetDetails.putExtra("user_screen_name", tweets.get(position).getUser().getScreenName());
                        tweetDetails.putExtra("user_pic_url", tweets.get(position).getUser().getProfileNameUrl());
                        tweetDetails.putExtra("tweet_object", Parcels.wrap(tweets.get(position)));
                        tweetDetails.putExtra("position", position);
                    }
                    startActivityForResult(tweetDetails, 1);
                }
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwitterClient client = TwitterApplication.getRestClient();
                client.getUserCredentials(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user_info_for_compose = User.fromJSON(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        if (statusCode == 200) {
                            alert_user("Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                        }
                    }
                }, null);

                TweetListFragment.this.onComposeAction(null);
            }
        });

                //Initialize array list and adapter

                // Adds the scroll listener to RecyclerView
                //      lvTweets.addOnScrollListener(scrollListener);
        return view;
    }

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        tweetsRecyclerAdapter = new TweetsRecyclerAdapter(getActivity(), tweets);

        //Repopulating on swiping
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int clickedPostion = data.getIntExtra("position", 0);
                boolean favorited = data.getBooleanExtra("wasFavorited", false);
                boolean retweeted = data.getBooleanExtra("wasRetweeted", false);
                tweets.get(clickedPostion).setFavorited(favorited);
                tweets.get(clickedPostion).setRetweeted(retweeted);
            }
        }
    }

    public void dataBindFragmentValues(FragmentTweetBinding binding) {
        lvTweets = binding.RecycleViewResultsId;
    //    swipeContainer = binding.SwipeContainerId;
        fab = binding.TimelineFabId;
        progressBar = binding.ProgressBarId;
        smoothProgressBar = binding.SmoothProgressBarId;
        fab = binding.TimelineFabId;
    }

    public void onComposeAction(String implicit_tweet) {
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                //Receive Implicit Intent
                //        sendImplicitIntentToCompose();

        Bundle bundle = new Bundle();

        if (user_info_for_compose != null) {
            bundle.putParcelable("user", user_info_for_compose);
        }
        bundle.putString("implicit_intent", implicit_tweet);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        TweetComposeFragment filterFragmentObject = TweetComposeFragment.newInstance();
        filterFragmentObject.setArguments(bundle);
        filterFragmentObject.show(fm, "fragment_edit_name");
            }}, 1000);
    }

    public void setupRecycleAdapter() {
        lvTweets.setAdapter(tweetsRecyclerAdapter);

        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lvTweets.setHasFixedSize(true);
        lvTweets.setLayoutManager(gridLayoutManager);
        lvTweets.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
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

    public void refreshCurrentTimeline() {
/*        //Repopulating the whole list to account for the deleted tweets
        int tweetArraySize = tweets.size();
        tweets.clear();
        tweetsRecyclerAdapter.notifyItemRangeRemoved(0, tweetArraySize);

        if(tweets.size() != 0) {
            sinceId = tweets.get(0).getUid();
        }
*/
        //tweetFetchClass.populateTimeline(tweets, tweetsRecyclerAdapter, client,
        //                                 sinceId, maxId, false);
        // swipeContainer.setRefreshing(false);
    }


}

package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.Activity.ActivityDetail;
import com.codepath.apps.mysimpletweets.Activity.ActivityTimeline.ItemClickSupport;
import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.Adapters.FollowAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.RecyclerItemDecorator;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

import static android.content.ContentValues.TAG;
import static com.codepath.apps.mysimpletweets.Fragments.TweetListFragment.NUM_GRID_COLUMNS;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class FollowProfileFragment extends Fragment {

    private User user;
    private TwitterClient client;
    private String type;
    public FollowAdapter followAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    public RecyclerView rvUsers;
    public StaggeredGridLayoutManager gridLayoutManager;
    private int cursor = -1;
    public ArrayList<User> mUsers;
    public Context mContext;
    public ProgressBar progressBar;
    public SmoothProgressBar smoothProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View v = inflater.inflate(R.layout.fragment_tweet, container, false);
        // Data binding

        FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        mContext = view.getContext();
        progressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity()).interpolator(new AccelerateInterpolator()).build());
        smoothProgressBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) smoothProgressBar.getIndeterminateDrawable()).getStrokeWidth()));

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        setupRecycleAdapter();
        //Displaying activity details. Using parcel
        ItemClickSupport.addTo(rvUsers).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent userDetails = new Intent(getActivity(), ActivityDetail.class);
                    if(mUsers.get(position) != null) {
                        String name = mUsers.get(position).getName();
                        Log.d(TAG, "USERRR is " + mUsers.get(position).getProfileNameUrl());
                        userDetails.putExtra("user_name", mUsers.get(position).getName());
                        userDetails.putExtra("user_screen_name", mUsers.get(position).getScreenName());
                        userDetails.putExtra("user_pic_url", mUsers.get(position).getProfileNameUrl());
                    //    tweetDetails.putExtra("tweet_object", Parcels.wrap(mUsers.get(position)));
                        userDetails.putExtra("position", position);
                    }
                    startActivityForResult(userDetails, 1);
                }
        );
        return view;
    }

    public void dataBindFragmentValues(FragmentTweetBinding binding) {
        rvUsers = binding.RecycleViewResultsId;
        progressBar = binding.ProgressBarId;
        smoothProgressBar = binding.SmoothProgressBarId;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup adapter
        mUsers = new ArrayList<>();
        followAdapter = new FollowAdapter(getActivity(), mUsers);
    }

    public void setupRecycleAdapter() {
        rvUsers.setAdapter(followAdapter);

        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(gridLayoutManager);
        rvUsers.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
    }
}
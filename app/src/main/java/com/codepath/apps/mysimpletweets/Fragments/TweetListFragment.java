package com.codepath.apps.mysimpletweets.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.RecyclerItemDecorator;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;

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
    private StaggeredGridLayoutManager gridLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    public long sinceId = -1, maxId = -1;
    private SwipeRefreshLayout swipeContainer;
    private boolean alreadyRefreshed = false;
   // private Toolbar toolbar;
    FloatingActionButton fab;
    User user_info_for_compose = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      // View v = inflater.inflate(R.layout.fragment_tweet, container, false);
      // Data binding

        FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        setupRecycleAdapter();

/*
        getActivity().setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
        //Declare the FAB button
     //   fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
     //           .setAction("Action", null).show());

        //Initialize array list and adapter


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        tweetsRecyclerAdapter = new TweetsRecyclerAdapter(getActivity(), tweets);

        //Repopulating on swiping
    }


    public void dataBindFragmentValues(FragmentTweetBinding binding) {
        lvTweets = binding.RecycleViewResultsId;
        swipeContainer = binding.SwipeContainerId;
        fab = binding.TimelineFabId;
    }

    public void setupRecycleAdapter() {
        lvTweets.setAdapter(tweetsRecyclerAdapter);

        lvTweets.setHasFixedSize(true);
        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lvTweets.setLayoutManager(gridLayoutManager);
        lvTweets.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
    }

    public void addAll(ArrayList<Tweet> inputTweets) {
        tweetsRecyclerAdapter.notifyItemRangeInserted(tweetsRecyclerAdapter.getItemCount(), inputTweets.size());
    }
}

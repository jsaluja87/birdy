package com.codepath.apps.mysimpletweets.Activity.ActivityTimeline;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;

/**
 * Created by jsaluja on 3/22/2017.
 */

public class EndlessScrolling {

    private EndlessRecyclerViewScrollListener mScrollListener;
    StaggeredGridLayoutManager mGridLayoutManger;
    RecyclerView mLvTweets;
    Context mContext;

    public EndlessScrolling(Context context, EndlessRecyclerViewScrollListener scrollListener, StaggeredGridLayoutManager gridLayoutManger, RecyclerView lvTweets) {
        mContext = context;
        mScrollListener = scrollListener;
        mGridLayoutManger = gridLayoutManger;
        mLvTweets = lvTweets;
    }

    public void setupEndlessScrolling() {
        mScrollListener = new EndlessRecyclerViewScrollListener(mGridLayoutManger) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(mContext, "LOADING MORE TWEETS", Toast.LENGTH_SHORT).show();
            }
        };
        // Adds the scroll listener to RecyclerView
        mLvTweets.addOnScrollListener(mScrollListener);

    }
}

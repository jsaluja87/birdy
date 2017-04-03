package com.codepath.apps.mysimpletweets.Activity.ActivityTimeline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Activity.ActivityDetail;
import com.codepath.apps.mysimpletweets.Activity.MessageActivity;
import com.codepath.apps.mysimpletweets.Activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.Activity.SearchActivity;
import com.codepath.apps.mysimpletweets.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.ProfileHeaderFragment;
import com.codepath.apps.mysimpletweets.Fragments.TweetListFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Fragments.TweetComposeFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity implements TweetComposeFragment.OnFragmentInteractionListener {
//    public class TimelineActivity extends AppCompatActivity implements TweetComposeFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TweetListFragment tweetListFragment;
    private boolean alreadyRefreshed = false;
    public ArrayList<Tweet> tweets;
    TweetsPagerAdapter tweetsPagerAdapter;
    private ImageView twitterIcon;

    private long sinceId = -1, maxId = -1;
 //   private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweetListFragment = new TweetListFragment();

        toolbar = (Toolbar)findViewById(R.id.toolbarId);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

 //       swipeContainer = (SwipeRefreshLayout)findViewById(R.id.SwipeContainerId)

        twitterIcon = (ImageView)findViewById(R.id.twitterIconImageViewId);
        //Get the viewpager
        sinceId = 5;
        ViewPager vpPager = (ViewPager)findViewById(R.id.timelineViewpagerId);

        //Set the viewpager adapter for the pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        tweetsPagerAdapter.setMaxId(maxId);
        tweetsPagerAdapter.setSinceId(sinceId);
        vpPager.setAdapter(tweetsPagerAdapter);
        //Find the sliding tab strip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.timelineTabsId);
        //Attach the tab strip to the viewpager
        tabStrip.setViewPager(vpPager);

        //Make sure user is populated before implicit intent is called
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                //Receive Implicit Intent
              //  sendImplicitIntentToCompose();
            }}, 2000);
/*
        //Repopulating on swiping

        swipeContainer.setOnRefreshListener(() -> {
            tweetListFragment.refreshCurrentTimeline();
            tweetsPagerAdapter.getItem(vpPager.getCurrentItem());
            Log.d(TAG, "vppager item is" +vpPager.getCurrentItem());
        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
*/
    }
/*
    public void sendImplicitIntentToCompose() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                onComposeAction(intent.getStringExtra("Tweety:Implicit_Intent"));
            }
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.MIActionSearchId);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                onTweetSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         //   case R.id.MIActionComposeId:onComposeAction(null);return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    public void onProfileView(MenuItem mi) {
        Intent profile = new Intent(this, ProfileActivity.class);
        //FIXME - Hack
        profile.putExtra("screen_name", "jsaluja87");
        startActivity(profile);
    }
    public void onMessagesView(MenuItem mi) {
        Intent viewMessage = new Intent(this, MessageActivity.class);
        startActivity(viewMessage);
    }

    public void onTweetSearch(String search) {
        twitterIcon.setVisibility(View.INVISIBLE);
        twitterIcon.setVisibility(View.GONE);
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra("query", search);
        startActivity(searchIntent);
    }

    public  void onFinishEditDialog(String newTweet, User user) {
/*
        Tweet tweet = new Tweet();
        tweet.setBody(newTweet);
        tweet.setUser(new User());
        if(user != null) {
            tweet.getUser().setName(user.getName());
            tweet.getUser().setScreenName(user.getScreenName());
            tweet.getUser().setProfileNameUrl(user.getProfileNameUrl());
        }

        tweetListFragment.tweets.add(0, tweet);
        tweetListFragment.tweetsRecyclerAdapter.notifyItemInserted(0);
        tweetListFragment.lvTweets.scrollToPosition(0);
*/
    }

}

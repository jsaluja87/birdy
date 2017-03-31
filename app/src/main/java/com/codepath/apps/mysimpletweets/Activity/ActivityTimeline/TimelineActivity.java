package com.codepath.apps.mysimpletweets.Activity.ActivityTimeline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.Adapters.TweetsRecyclerAdapter;
import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.TweetListFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Fragments.TweetComposeFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity implements TweetComposeFragment.OnFragmentInteractionListener {
/*
    //FIXME - read all hints
    final static int NUM_GRID_COLUMNS = 1;
    final static String TAG = "TIMELINE_LOG";
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsRecyclerAdapter tweetsRecyclerAdapter;
    private RecyclerView lvTweets;
    private StaggeredGridLayoutManager gridLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private long sinceId = -1, maxId = -1;
    private SwipeRefreshLayout swipeContainer;
    private boolean alreadyRefreshed = false;
    FloatingActionButton fab;
    User user_info_for_compose = null;
*/

    private Toolbar toolbar;
    private TweetListFragment tweetListFragment;
    private boolean alreadyRefreshed = false;
    private RecyclerView lvTweets;
    User user_info_for_compose = null;
    TweetsRecyclerAdapter tweetsRecyclerAdapter;
    public ArrayList<Tweet> tweets;
    final static String TAG = "TIMELINE_LOG";

    private long sinceId = -1, maxId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = (Toolbar)findViewById(R.id.toolbarId);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Get the viewpager
        ViewPager vpPager = (ViewPager)findViewById(R.id.timelineViewpagerId);
        //Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //Find the sliding tab strip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.timelineTabsId);
        //Attach the tab strip to the viewpager
        tabStrip.setViewPager(vpPager);
/*
        if(savedInstanceState == null) {
            tweetListFragment = (TweetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTimelineId);
        }
        tweetsRecyclerAdapter = tweetListFragment.tweetsRecyclerAdapter;
        tweets = tweetListFragment.tweets;
        lvTweets = tweetListFragment.lvTweets;
        //Get rest client and send initial request
        //Setting since_id to a random value of 5. Fetch the first page
        sinceId = 5; maxId = -1;
        TweetFetchClass tweetFetchClass = new TweetFetchClass(TimelineActivity.this);
        tweetFetchClass.populateTimeline(tweets, tweetsRecyclerAdapter,
                                         sinceId, maxId, false);
        alreadyRefreshed = false;
        sinceId = -1;
*/

/*
        //Get user's details
        getUserCredentials();
*/
        //Make sure user is populated before implicit intent is called
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                //Receive Implicit Intent
                sendImplicitIntentToCompose();
            }}, 2000);

        //Repopulating on swiping
/*        swipeContainer.setOnRefreshListener(() -> {

            //Repopulating the whole list to account for the deleted tweets
            int tweetArraySize = this.tweets.size();
            this.tweets.clear();
            tweetsRecyclerAdapter.notifyItemRangeRemoved(0, tweetArraySize);

            if(tweets.size() != 0) {
                sinceId = tweets.get(0).getUid();
            }

            //If found a way to delete deleted tweets without entire refresh
            //Use following code to get rest of the values
           // if(!alreadyRefreshed) {
            //    Log.d(TAG, "tweets details "+ tweets.size());
            //    maxId = tweets.get(tweets.size() - 1).getUid();
           //     alreadyRefreshed = true;
           // } else {
           //     maxId = -1;
           // }
            tweetFetchClass.populateTimeline(tweets, tweetsRecyclerAdapter, client,
                                             sinceId, maxId, false);
            swipeContainer.setRefreshing(false);
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Displaying activity details. Using parcel
        ItemClickSupport.addTo(lvTweets).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent tweetDetails = new Intent(TimelineActivity.this, ActivityDetail.class);
                    if(tweets.get(position).getUser() != null) {
                        String name = tweets.get(position).getUser().getName();
                        Log.d(TAG, "USERRR is " + tweets.get(position).getUser().getProfileNameUrl());
                        tweetDetails.putExtra("user_name", tweets.get(position).getUser().getName());
                        tweetDetails.putExtra("user_screen_name", tweets.get(position).getUser().getScreenName());
                        tweetDetails.putExtra("user_pic_url", tweets.get(position).getUser().getProfileNameUrl());
                        tweetDetails.putExtra("tweet_object", Parcels.wrap(tweets.get(position)));
                    }
                    startActivity(tweetDetails);
                }
        );

        //Inifinite Scroll
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                maxId = tweets.get(tweets.size() - 1).getUid() - 1;
                tweetFetchClass.populateTimeline(tweets, tweetsRecyclerAdapter, client,
                                                 sinceId, maxId, false);
                sinceId = -1; maxId = -1;
            }


        };

        // Adds the scroll listener to RecyclerView
        lvTweets.addOnScrollListener(scrollListener);
        //Starting compose activity
        fab.setOnClickListener(v -> onComposeAction(null));

*/
    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
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
        startActivity(profile);
    }

    /*
    private void getUserCredentials() {
        client.getUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                user_info_for_compose = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                user_info_for_compose = null;
                InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(TimelineActivity.this);
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
        });
    }
*/
    public void onComposeAction(String implicit_tweet) {
        Bundle bundle = new Bundle();

        if (user_info_for_compose != null) {
            bundle.putParcelable("user", user_info_for_compose);
        }
        bundle.putString("implicit_intent", implicit_tweet);

        FragmentManager fm = getSupportFragmentManager();
        TweetComposeFragment filterFragmentObject = TweetComposeFragment.newInstance();
        filterFragmentObject.setArguments(bundle);
        filterFragmentObject.show(fm, "fragment_edit_name");
    }

    public  void onFinishEditDialog(String newTweet, User user) {
        //FIXME - How to manually insert the tweet in timeline??
        Tweet tweet = new Tweet();
        tweet.setBody(newTweet);
        tweet.setUser(new User());
        if(user != null) {
            tweet.getUser().setName(user.getName());
            tweet.getUser().setScreenName(user.getScreenName());
            tweet.getUser().setProfileNameUrl(user.getProfileNameUrl());
        }
        tweets.add(0, tweet);
        tweetsRecyclerAdapter.notifyItemInserted(0);
        lvTweets.scrollToPosition(0);

    }

    //Return order of fragments for viewPager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};
        //Adapter gets the manger insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            //    return "HomeTimelineFragment";
            } else if(position == 1){
                return new MentionsTimelineFragment();
            //    return "MentionsTimelineFragment";
            } else {
                return null;
            }
        }
        //Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        //Number of fragment
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}

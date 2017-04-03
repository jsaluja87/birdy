package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Activity.MessageActivity;
import com.codepath.apps.mysimpletweets.Adapters.MessagesAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.SupportingClasses.RecyclerItemDecorator;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

import static com.codepath.apps.mysimpletweets.Fragments.TweetListFragment.NUM_GRID_COLUMNS;

/**
 * Created by jsaluja on 4/1/2017.
 */

public class MessageResultFragment extends Fragment {
    private final static String TAG = "Message Fragment";
    TwitterClient client;
    Context mContext;
    View mView;
    ArrayList<Message> messages;
    MessagesAdapter messagesAdapter;
    public RecyclerView rvMessages;
    public StaggeredGridLayoutManager gridLayoutManager;
    public ProgressBar progressBar;
    public SmoothProgressBar smoothProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        View view = binding.getRoot();

        rvMessages = binding.RecycleViewResultsId;

        mView = view;
        mContext = view.getContext();
        progressBar = binding.ProgressBarId;
        smoothProgressBar = binding.SmoothProgressBarId;
        progressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity()).interpolator(new AccelerateInterpolator()).build());
        smoothProgressBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) smoothProgressBar.getIndeterminateDrawable()).getStrokeWidth()));

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        client = TwitterApplication.getRestClient();
        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(mContext, messages);

        setupRecycleAdapter();

        rvMessages = binding.RecycleViewResultsId;
        //  FragmentTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false);
        //  View view = binding.getRoot();

        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        client = TwitterApplication.getRestClient();
        if (internetAlertDialogue.checkForInternet()) {
            populateMessages();
        }
        return view;
    }

    public void setupRecycleAdapter() {
        rvMessages.setAdapter(messagesAdapter);

        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(gridLayoutManager);
        rvMessages.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
    }

    public void populateMessages() {

        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        client.getDirectMessages(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d(TAG, "Response is " + response.toString());
                messages.addAll(Message.fromJSONArray(response));
                messagesAdapter.notifyItemRangeInserted(messagesAdapter.getItemCount(), messages.size());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "Response failed");
                if (statusCode == 200) {
                    alert_user("Home Timeline Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                }
            }
        });
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


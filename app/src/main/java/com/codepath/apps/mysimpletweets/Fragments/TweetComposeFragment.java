package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.mysimpletweets.Activity.ActivityDetail;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
//import com.codepath.apps.mysimpletweets.databinding.FragmentTweetComposeBinding;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetComposeBinding;
import com.codepath.apps.mysimpletweets.SupportingClasses.TweetComposeDraftClass;
import com.codepath.apps.mysimpletweets.SupportingClasses.ImplicitIntent;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetComposeFragment extends DialogFragment {

    final static int MAX_TWEET_CHAR_ALLOWED=140;
    private ImageView userImage;
    private TextView userName, userScreenName;
    public EditText tweetComposeView;
    TwitterClient client;
    public Button tweetSaveButton;
    public ImageButton tweetCancelButton;
    private TextView tweetTextCount;
    private final static String TAG = "TweetFragment";
    String implicit_intent = null;
    User user = new User();
    int replyTweet;

    TweetComposeDraftClass tweetComposeDraftClass = new TweetComposeDraftClass(getActivity());
    public OnFragmentInteractionListener mListener;

    public TweetComposeFragment() {
        // Required empty public constructor
    }

    public static TweetComposeFragment newInstance() {
        TweetComposeFragment fragment = new TweetComposeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static TweetComposeFragment newInstance(ImplicitIntent implicitIntent) {
        TweetComposeFragment fragment = new TweetComposeFragment();
        Bundle args = new Bundle();
        args.putParcelable("implicit_intent", (Parcelable) implicitIntent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentTweetComposeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_compose, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        TweetComposeDraftClass tweetComposeDraftClass = new TweetComposeDraftClass(getActivity());

        userImage = binding.ComposeUserImageId;
        userName = binding.ComposeUserNameId;
        userScreenName = binding.ComposeUserScreenNameId;
        tweetComposeView = binding.ComposeEditTextId;
        tweetSaveButton = binding.ComposeSaveButtonId;
        tweetTextCount = binding.tweetTextCountId;
        tweetCancelButton = binding.CancelButtonId;


        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
            implicit_intent = bundle.getString("implicit_intent");
            replyTweet = bundle.getInt("reply_tweet");


            if(user != null){
                Glide.with(getContext()).load(user.getProfileNameUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
                userName.setText(user.getName());
                userScreenName.setText("@" + user.getScreenName());
            }

        }

        if(implicit_intent != null) {
            tweetComposeView.setText(implicit_intent);
        } else if(replyTweet == 1) {
            tweetComposeView.setText("@"+user.getScreenName());
        } else {
            tweetComposeView.setText(tweetComposeDraftClass.checkForOldTweetDraft());
        }

        tweetComposeView.setSelection(tweetComposeView.getText().length());

        this.setCancelable(false);
        tweetSaveButton.setOnClickListener(v -> {
            onSendAction();
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishEditDialog(tweetComposeView.getText().toString(), user);
            dismiss();
        });

        tweetComposeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textCount = (MAX_TWEET_CHAR_ALLOWED-count);
                if(textCount < 0) {
                    Toast.makeText(getActivity(), "Tweet Max character exceeded!! Please limit to 140 character!!", Toast.LENGTH_SHORT).show();
                    tweetTextCount.setText("0");
                    tweetTextCount.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorRed));

                    tweetSaveButton.setOnClickListener(null);
                    tweetSaveButton.setClickable(false);
                } else {
                    tweetTextCount.setText(""+textCount);

                    tweetSaveButton.setClickable(true);
                    tweetSaveButton.setOnClickListener(v -> {
                        onSendAction();
                        mListener = (OnFragmentInteractionListener) getActivity();
                        mListener.onFinishEditDialog(tweetComposeView.getText().toString(), user);
                        dismiss();
                    });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tweetCancelButton.setOnClickListener(v -> {
            if(!tweetComposeView.getText().toString().equals("")) {
                tweetComposeDraftClass.alertTweetDraft("Save Tweet?", "Would you like to save your tweet?", tweetComposeView.getText().toString());
            }
            dismiss();
        });
        tweetCancelButton.setOnClickListener(v -> {
            if(!tweetComposeView.getText().toString().equals("")) {
                tweetComposeDraftClass.alertTweetDraft("Save Tweet?", "Would you like to save your tweet?", tweetComposeView.getText().toString());
            }
            dismiss();
        });

        return view;
    }

    private void dataBindFragmentValues(FragmentTweetComposeBinding binding) {
        tweetComposeView = binding.ComposeEditTextId;
        tweetSaveButton = binding.ComposeSaveButtonId;
        tweetTextCount = binding.tweetTextCountId;
    }

    @Override
    public void onAttach(Context context) {
        context = getContext();
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFinishEditDialog(String newTweet, User user);
    }

    public void onSendAction() {

        if(tweetComposeView.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Please enter a valid Tweet!", Toast.LENGTH_SHORT).show();
        } else {
            client = TwitterApplication.getRestClient();
            client.postStatuses(new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, "TWEET POST SUCCESS");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    //FIXME - Check for internet and network
                    Log.d(TAG, "TWEET POST FAILED");
                    InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(getContext());
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
            }, tweetComposeView.getText().toString());
        }


    }

    public void alert_user(String title, String message) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setNegativeButton("Ok",
                (dialog1, which) -> dialog1.cancel());
        AlertDialog alertD = dialog.create();
        alertD.show();
    }
}
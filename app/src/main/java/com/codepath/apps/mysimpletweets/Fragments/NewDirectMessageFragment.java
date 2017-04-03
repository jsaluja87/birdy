package com.codepath.apps.mysimpletweets.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.MessagesAdapter;
import com.codepath.apps.mysimpletweets.Applications.TwitterApplication;
import com.codepath.apps.mysimpletweets.Clients.TwitterClient;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SupportingClasses.ImplicitIntent;
import com.codepath.apps.mysimpletweets.SupportingClasses.InternetAlertDialogue;
import com.codepath.apps.mysimpletweets.SupportingClasses.TweetComposeDraftClass;
import com.codepath.apps.mysimpletweets.databinding.FragmentSendDirectMessageBinding;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetBinding;
import com.codepath.apps.mysimpletweets.databinding.FragmentTweetComposeBinding;
import com.codepath.apps.mysimpletweets.models.Message;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by jsaluja on 4/2/2017.
 */

public class NewDirectMessageFragment extends DialogFragment {
    private final static String TAG = "Direct Message Fragment";
    TwitterClient client;
    Context mContext;
    View mView;
    private EditText userName;
    private EditText message;
    private Button cancel;
    private Button send;
    private String receiverScreenName = null;


    public static NewDirectMessageFragment newInstance(String screenName) {
        NewDirectMessageFragment fragment = new NewDirectMessageFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSendDirectMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_direct_message, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);


        Bundle bundle = getArguments();
        if (bundle != null) {
            receiverScreenName = bundle.getString("screen_name");
        }
        mView = view;
        mContext = view.getContext();

        client = TwitterApplication.getRestClient();
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        Log.d(TAG, "direct message screen_name is "+receiverScreenName);
        if(receiverScreenName != null) {
            userName.setText("@" + receiverScreenName);
        } else {

            userName.setText("@");
        }

        userName.setSelection(userName.getText().length());

        send.setOnClickListener(v -> {
            if (internetAlertDialogue.checkForInternet()) {
                sendDirectMessage(message.getText().toString(), userName.getText().toString());
                dismiss();
            }
        });
        cancel.setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }

    public void sendDirectMessage(String messageText, String receiverScreenName) {

        client.sendDirectMessage(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "Response is " + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "Response failed");
                if (statusCode == 200) {
                    alert_user("Home Timeline Result fetch failed!", "The request sent out is good. Response failed from the website!!!");
                }
            }
        }, messageText, receiverScreenName);

    }

    private void dataBindFragmentValues(FragmentSendDirectMessageBinding binding) {
        userName = binding.MessageUserEditTextId;
        message = binding.MessageComposeEditTextId;
        send = binding.MessageSaveButtonId;
        cancel = binding.MessageCancelButtonId;

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

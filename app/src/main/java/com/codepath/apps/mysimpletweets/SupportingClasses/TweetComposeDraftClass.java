package com.codepath.apps.mysimpletweets.SupportingClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.R.attr.data;
import static android.R.id.message;

/**
 * Created by jsaluja on 3/26/2017.
 */

public class TweetComposeDraftClass {

    Context mContext;
    public TweetComposeDraftClass(Context context) {
        mContext = context;
    }

    public void alertTweetDraft(String title, String message, String draftedTweet) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Save",
                (dialog1, which) -> {
                    saveTweetToFile(draftedTweet, mContext);
                    dialog1.cancel();
                });
        dialog.setNegativeButton("Delete",
                (dialog12, which) -> {
                    saveTweetToFile("", mContext);
                    dialog12.cancel();
                });
        AlertDialog alertD = dialog.create();
        alertD.show();
    }

    public void saveTweetToFile(String data,Context mContext) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("tweet_draft.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String checkForOldTweetDraft() {
        String ret = "";

        try {
            InputStream inputStream = mContext.openFileInput("tweet_draft.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }
}

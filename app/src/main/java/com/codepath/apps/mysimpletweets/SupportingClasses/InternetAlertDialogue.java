package com.codepath.apps.mysimpletweets.SupportingClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import java.io.IOException;

/**
 * Created by jsaluja on 3/26/2017.
 */

public class InternetAlertDialogue {
    Context mContext;
    public InternetAlertDialogue(Context context) {
        mContext = context;
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
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

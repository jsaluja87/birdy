package com.codepath.apps.mysimpletweets.SupportingClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.sqlitehelper.OfflineTweetClass;

import java.io.IOException;

/**
 * Created by jsaluja on 3/26/2017.
 */

public class InternetAlertDialogue {
    private Context mContext;
    public InternetAlertDialogue(Context context) {
        mContext = context;
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable =  activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if(!networkAvailable) alert_user("Network Issue", "Please connect the device to an internet network!");

        return networkAvailable;
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        boolean deviceIsOnline;
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            deviceIsOnline =  (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); deviceIsOnline = false;}
        catch (InterruptedException e) { e.printStackTrace(); deviceIsOnline = false;}

        if(!deviceIsOnline) alert_user("Network Issue", "Device network does not have internet access!");

        return deviceIsOnline;


    }

    public boolean checkForInternet() {
        Log.d("Internet Dialog", "isOnline() = "+isOnline()+ "isNetworkAvailable = "+isNetworkAvailable());
        if(isOnline() && isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
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

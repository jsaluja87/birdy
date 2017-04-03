package com.codepath.apps.mysimpletweets.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.Adapters.MessagesAdapter;
import com.codepath.apps.mysimpletweets.Fragments.MessageResultFragment;
import com.codepath.apps.mysimpletweets.Fragments.NewDirectMessageFragment;
import com.codepath.apps.mysimpletweets.Fragments.SearchResultFragment;
import com.codepath.apps.mysimpletweets.Fragments.TweetListFragment;
import com.codepath.apps.mysimpletweets.R;

import static android.R.id.message;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private final static String TAG = "Message Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = (Toolbar)findViewById(R.id.messageToolbarId);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);


        if(savedInstanceState == null) {
            MessageResultFragment messageResultFragment = new MessageResultFragment();
            //Dynamically display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.FLMessageContainerId, messageResultFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
     //   return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MessageMIComposeId: onComposeDirectMessage();
            //   case R.id.MIActionComposeId:onComposeAction(null);return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    public void onComposeDirectMessage() {
        FragmentManager fm = this.getSupportFragmentManager();
        NewDirectMessageFragment newDirectMessageFragment = NewDirectMessageFragment.newInstance(null);
        newDirectMessageFragment.show(fm, "fragment_new_message");
    }

}

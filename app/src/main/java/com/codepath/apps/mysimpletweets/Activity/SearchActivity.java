package com.codepath.apps.mysimpletweets.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.mysimpletweets.Fragments.ProfileHeaderFragment;
import com.codepath.apps.mysimpletweets.Fragments.SearchResultFragment;
import com.codepath.apps.mysimpletweets.R;

import static com.codepath.apps.mysimpletweets.models.sqlitehelper.Organization_Table.screenName;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private final static String TAG = "Search Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar)findViewById(R.id.searchToolbarId);
        this.setSupportActionBar(toolbar);

        setContentView(R.layout.activity_search);
        String query = getIntent().getStringExtra("query");

//       / getSupportActionBar().setTitle("Search Results");
        if(savedInstanceState == null) {
            SearchResultFragment searchResultFragment = SearchResultFragment.newInstance(query);
            //Dynamically display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.FLSearchContainerId, searchResultFragment);
            ft.commit();
        }
    }
}

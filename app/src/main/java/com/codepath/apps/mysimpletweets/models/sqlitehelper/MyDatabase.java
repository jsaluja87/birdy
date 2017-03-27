package com.codepath.apps.mysimpletweets.models.sqlitehelper;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

    public static final String NAME = "TwitterClientDatabase";

    public static final int VERSION = 1;

}

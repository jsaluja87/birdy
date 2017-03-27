package com.codepath.apps.mysimpletweets.models.sqlitehelper;

/**
 * Created by jsaluja on 3/25/2017.
 */

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

// **Note:** Your class must extend from BaseModel
// Make sure that your table names are upper camel case (Organization, OrganizationTable, etc.) for DBFlow3
@Table(database = MyDatabase.class)
@Parcel(analyze={MyDatabase.class})
public class Organization extends BaseModel {
    // ... field definitions that map to columns go here ...
    //FIXME - try individual fields to see if it works. Then try http://stackoverflow.com/questions/33825496/one-to-many-in-db-flow
    @Column
    @PrimaryKey
    int id;

    @Column
    String name;

    @Column
    String screenName;

    @Column
    String createdAt;

    @Column
    String body;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
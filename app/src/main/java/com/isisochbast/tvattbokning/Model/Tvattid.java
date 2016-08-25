package com.isisochbast.tvattbokning.Model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyMetaData;

/**
 * Mappa Java till Kinvey
 * Klass för tvättiden
 */
public class Tvattid extends GenericJson {


    private boolean mSolved;


    @Key("_id")
    private String id;
    @Key
    private String date;

    @Key("_kmd")
    private KinveyMetaData meta; // Kinvey metadata, OPTIONAL

    @Key("_acl")
    private KinveyMetaData.AccessControlList acl; //Kinvey access control, OPTIONAL

    public String getDate() {
        return date;
    }


    public void setDate(String newDate) {
        date = newDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        id = newId;
    }

    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
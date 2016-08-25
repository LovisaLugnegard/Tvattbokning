package com.isisochbast.tvattbokning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kinvey.android.Client;

/*
 En abstrakt superklass till fragmenten, har metoderna getClient och getUser, hämtar denna info från
 TvattbokningApp
 */
public abstract class KinveyFragment extends Fragment {



    final static String TAG = "test";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public Client getClient(){
        return TvattbokningApp.getClient();
    }

    public String getUser(){
        return TvattbokningApp.getUserId();
    }
}

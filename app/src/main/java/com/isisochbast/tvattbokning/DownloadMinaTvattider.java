package com.isisochbast.tvattbokning;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class DownloadMinaTvattider extends AsyncTask<Void, Void, ArrayList<Tvattid>> {

    String TAG = "test";

    ProgressDialog mProgressDialog;
    Context mContext;
    ArrayList<Tvattid> mArrayList;


    DownloadMinaTvattider(Context context) {
        //this.mArrayList = arrayList;
        this.mContext = context;
    }



    @Override
    protected ArrayList<Tvattid> doInBackground(Void... params) {
        Log.d(TAG, "TL getMinaTvattider");


        // mMinaTvattider.clear();
        Log.i(TAG, "TL getMinaTvattider mMinaTvattider: " + mArrayList.size());


        Client mClient = TvattbokningApp.getClient();

//        Log.i(TAG, "" + mClient.user().isUserLoggedIn());

        AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);


        Query q = new Query().equals("_acl.creator", mClient.user().getId());

        myData.get(q, new KinveyListCallback<Tvattid>() {

            @Override

            public void onSuccess(Tvattid[] result) {
                //  mMinaTvattider = result;
                Log.d(TAG, "DMT on Success" + result.length);

                mArrayList.clear();
                mArrayList.addAll(Arrays.asList(result));

                Log.d(TAG, "DMT MT storlek: " + mArrayList.size());

//got entities created by the current user

            }


            @Override

            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");

//something went wrong!

            }

        });
        return mArrayList;
    }

    @Override
    protected void onPreExecute() {

        mArrayList = new ArrayList<>();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("Download in progress");
        mProgressDialog.setMax(10);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Tvattid> result) {



        mProgressDialog.hide();
        Toast.makeText(mContext, "Download complete", Toast.LENGTH_LONG).show();
        MinaTvattiderFragment m = new MinaTvattiderFragment();
        notify();
        //m.setTvattider(result);


    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

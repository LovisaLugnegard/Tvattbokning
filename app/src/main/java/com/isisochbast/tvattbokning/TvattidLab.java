package com.isisochbast.tvattbokning;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TvattidLab {
    private static String TAG = "test";

    //    static List<Tvattid> mMinaTvattider = new ArrayList<>();
    // private ArrayList<Tvattid> mMinaTvattider;
    private static TvattidLab sTvattidLab;
    private ArrayList<Tvattid> mMinaTvattider;
    static Context mContext;
    ProgressBar mProgressBar;


    public static TvattidLab get(Context context) {
        Log.d(TAG, "i TvattiLab get");
        mContext = context;


        if (sTvattidLab == null) {
            sTvattidLab = new TvattidLab(context);
        }
        return sTvattidLab;
    }

    private TvattidLab(Context context) {
//        mProgressBar.findViewById(R.id.progressBar);
        mMinaTvattider = new ArrayList<>();
        Log.d(TAG, "i TvattidLab");

        //     mMinaTvattider = new ArrayList<>();


        //  Log.i(TAG, "TL TvattidLab mMinaTvattider: " + mMinaTvattider.size());

        //denna kod borde vara någonannanstans, just nu nås den bara när TvättidLab skapas, dvs en gång/session
/*

        Client mClient = TvattbokningApp.getClient();
        Query q = new Query().equals("_acl.creator", mClient.user().getId());

        //vad är skillnaden mellan appData och AppSyncData?
        mClient.appData("events", Tvattid.class).get(q, new KinveyListCallback<Tvattid>() {

            @Override

            public void onSuccess(Tvattid[] result) {
              //  mMinaTvattider = result;
                Log.d(TAG, "TL on Success" + result.length);

                mMinaTvattider.addAll(Arrays.asList(result));

                Log.d(TAG, "TL MT storlek: " + mMinaTvattider.size());

//got entities created by the current user

            }


            @Override

            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");

//something went wrong!

            }

        });*/


        // return tvattidLab;
    }


  /*  public List<Tvattid> getMinaTvattider() {

        Log.i(TAG, "TL getMinaTvattider innan return mMinaTvattider: " + mMinaTvattider.size());
       setMinaTvattider();
       /* DownloadMinaTvattider downloadMinaTvattider = new DownloadMinaTvattider(mContext);
        downloadMinaTvattider.execute();*/
       /* return mMinaTvattider;
    }*/

    public void getMinaTvattider(){
        Log.d(TAG, "TL getMinaTvattider");

      //  mProgressBar.setVisibility(View.VISIBLE);


        // mMinaTvattider.clear();
      //  Log.i(TAG, "TL getMinaTvattider mMinaTvattider: " + mMinaTvattider.size());


        Client mClient = TvattbokningApp.getClient();

//        Log.i(TAG, "" + mClient.user().isUserLoggedIn());

        AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);


        Query q = new Query().equals("_acl.creator", mClient.user().getId());

        myData.get(q, new KinveyListCallback<Tvattid>() {

            @Override

            public void onSuccess(Tvattid[] result) {
                //  mMinaTvattider = result;
                Log.d(TAG, "TL on Success" + result.length);

                mMinaTvattider.clear();
                mMinaTvattider.addAll(Arrays.asList(result));

                Log.d(TAG, "TL MT storlek: " + mMinaTvattider.size());
                MinaTvattiderFragment m = new MinaTvattiderFragment();
              //  m.setMinaTvattidervattider(mMinaTvattider);
                //mProgressBar.setVisibility(View.GONE);


//got entities created by the current user

            }


            @Override

            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");

//something went wrong!

            }

        });
    }



    public Tvattid getTvattid(String id) {
        for (Tvattid tvattid : mMinaTvattider) {
            if (tvattid.getId().equals(id)) {
                return tvattid;
            }
        }
        return null;
    }
}

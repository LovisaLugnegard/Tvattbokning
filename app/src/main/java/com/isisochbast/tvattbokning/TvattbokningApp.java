package com.isisochbast.tvattbokning;

import android.app.Application;
import android.util.Log;

import com.kinvey.android.Client;


//här hämtas KinveyClient

//vad händer om jag lägger allt detta i kinveyfragment? obs! alla ärver inte KF
public class TvattbokningApp extends Application {
    private static final String TAG = "test";

    private static Client sClient;
    private static String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "TA onCreate");
        defineClient();
    }


    private void defineClient() {
        Log.d(TAG, "define client");
        sClient = new Client.Builder("kid_-k-V-tmSxb", "04e6968a09314c22baecd51ba5a0818f", getApplicationContext()).build();
        //   sClient.enableDebugLogging();

    }


    //}


    public static Client getClient() {
        Log.d(TAG, "TA client: " + sClient + sClient.user().isUserLoggedIn());
        Log.d(TAG, "TA getClient, isloggedIn: " + sClient.user().isUserLoggedIn());
        return sClient;

    }

    public static String getUserId() {
        userId = sClient.user().getId();
        Log.d(TAG, "TA getUserId, isloggedIn: " + userId + sClient.user().isUserLoggedIn());
        return userId;
    }
}

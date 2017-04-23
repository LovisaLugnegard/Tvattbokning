package com.isisochbast.tvattbokning;

import android.app.Application;

import com.kinvey.android.Client;

//här hämtar KinveyClient

//vad händer om jag lägger allt detta i kinveyfragment? obs! alla ärver inte KF
public class TvattbokningApp extends Application {

    private static Client sClient;

    @Override
    public void onCreate() {
        super.onCreate();
        defineClient();
    }

    private void defineClient() {
        sClient = new Client.Builder("kid_-k-V-tmSxb", "04e6968a09314c22baecd51ba5a0818f",
                getApplicationContext()).build();
    }

    public static Client getClient() {
        return sClient;
    }

    public static String getUserId() {
        return sClient.user().getId();
    }
}

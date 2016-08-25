package com.isisochbast.tvattbokning;

/*
* Är startaktiviteten, testar internetuppkoppling, startar StartFragment mha abtrakta klassen SingleActivityFragment*/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class StartActivity extends SingleFragmentActivity {


//    private static final Level LOGGING_LEVEL = Level.FINEST;


    @Override
    protected Fragment createFragment() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "Appen kräver internetanslutning", Toast.LENGTH_LONG).show();
        }

        //Logger.getLogger(HttpTransport.class.getName()).setLevel(LOGGING_LEVEL);

        //on anv är inloggad, gå direkt till dashboard, annrs, gå till logga in
        if (TvattbokningApp.getClient().user().isUserLoggedIn()) {
            return new DashboardFragment();
        } else {
            return new StartFragment();
        }

    }


    //on* är för att se vad som händer, ta bort dem senare!
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "SA onStart called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "SA onPause called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "SA onResume called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SA onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SA onDestroy called");
    }
}

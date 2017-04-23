package com.isisochbast.tvattbokning;

/*
* Är startaktiviteten, testar internetuppkoppling, startar StartFragment mha abtrakta klassen SingleActivityFragment*/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class StartActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.noConnectionString, Toast.LENGTH_LONG).show();
        }

        //om anv är inloggad, gå direkt till dashboard, annars, gå till logga in
        if (TvattbokningApp.getClient().user().isUserLoggedIn()) {
            return new DashboardFragment();
        } else {
            return new StartFragment();
        }
    }
}

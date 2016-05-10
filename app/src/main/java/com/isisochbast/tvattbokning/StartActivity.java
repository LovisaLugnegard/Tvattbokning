package com.isisochbast.tvattbokning;

/*
* Är startaktiviteten, testar internetuppkoppling, startar StartFragment*/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            Toast.makeText(getApplicationContext(), "Appen kräver internetanslutning", Toast.LENGTH_LONG).show();
        }

        //Hantera fragment
        FragmentManager fm = getSupportFragmentManager();
        //fragment_container är namnet på frameLayout i fragment_holder.xmll
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //Om det inte finns något fragment än (första gången onCreate körs), hämta nytt StartFragment
        if(fragment == null){
            fragment = new StartFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}

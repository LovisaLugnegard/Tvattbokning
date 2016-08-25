package com.isisochbast.tvattbokning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * En abstrakt klass för att återvinna koden som krävs till för att para ihop fragment och aktiviteter
 * -här kan jag nog lägga in lite mer grejs
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    final String TAG = "test";

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);


        Log.d(TAG, "SFA" + TvattbokningApp.getClient().user().isUserLoggedIn());

        //Hantera fragment
        FragmentManager fm = getSupportFragmentManager();
        //fragment_container är namnet på frameLayout i fragment_holder.xmll
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //Om det inte finns något fragment än (första gången onCreate körs), hämta nytt StartFragment
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    //on* är för att se vad som händer, ta bort dem senare!
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "SFA onStart called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "SFA onPause called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "SFA onResume called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SFA onStop called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SFA onDestroy called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }
}
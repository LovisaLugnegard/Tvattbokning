package com.isisochbast.tvattbokning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * En abstrakt klass för att återvinna koden som krävs till för att para ihop fragment och aktiviteter
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    final String TAG = "test";

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Hantera fragment
        FragmentManager fm = getSupportFragmentManager();
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
        //Log.d(TAG, "SFA onStart called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onPause() {
        super.onPause();
        //  Log.d(TAG, "SFA onPause called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onResume() {
        super.onResume();
        //  Log.d(TAG, "SFA onResume called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onStop() {
        super.onStop();
        //  Log.d(TAG, "SFA onStop called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.d(TAG, "SFA onDestroy called" + TvattbokningApp.getClient().user().isUserLoggedIn());
    }
}
package com.isisochbast.tvattbokning;

/*Aktivitet för att hantera dashboard, där användaren kan göra valen att visa lediga tider, sina bokningar eller logga ut
 denna aktivitet håller i fragmentet som sköter detta*/


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by Lovisa on 2016-05-04.
 */
public class DashboardActivity extends FragmentActivity{
    final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        Log.i(TAG, "i DashboardActivity");
    setContentView(R.layout.fragment_holder);


    //Hantera fragment
    FragmentManager fm = getSupportFragmentManager();
    //fragment_container är namnet på frameLayout i fragment_holder.xmll
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);

    //Om det inte finns något fragment än (första gången onCreate körs), hämta nytt StartFragment
    if(fragment == null){
        fragment = new DashboardFragment();
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}}

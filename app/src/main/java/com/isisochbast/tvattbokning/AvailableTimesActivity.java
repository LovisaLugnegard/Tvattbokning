package com.isisochbast.tvattbokning;


//Hit kommer en från dashboard när en klickar på lediga tider, syftet är att visa de lediga tider och maskiner som finns
//denna klass använder fragment, så det mesta sker i Availabletimesfragment


import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AvailableTimesActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        String daySelected;
        daySelected = extras.getString("date");
        Bundle bundle = new Bundle();
        bundle.putString("date", daySelected);
        AvailableTimesFragment availableTimesFragment = new AvailableTimesFragment();
        availableTimesFragment.setArguments(bundle);
        return availableTimesFragment;
    }
}

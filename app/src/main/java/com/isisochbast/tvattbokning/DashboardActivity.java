package com.isisochbast.tvattbokning;

/*Aktivitet för att hantera dashboard, där användaren kan göra valen att visa lediga tider, sina bokningar eller logga ut
 denna aktivitet håller i fragmentet som sköter detta, fragment hämtas mha den abstrakta klassen SingleFragmentActivity*/


import android.support.v4.app.Fragment;

public class DashboardActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DashboardFragment();
    }
}

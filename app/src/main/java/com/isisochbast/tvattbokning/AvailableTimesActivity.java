package com.isisochbast.tvattbokning;

//DENNA AKTIVITET ANVÄNDS INTE JUST NU, ISTÄLLET ÄR DET EN DATEDIALOGGREJ I DASHBOARD - ta bort detta om detta ändras:)

//Hit kommer en från dashboard när en klickar på lediga tider, syftet är att visa de lediga tider och maskiner som finns
//denna klass använder fragment, så det mesta sker i Availabletimesfragment

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.os.Bundle;
        import android.util.Log;

        public class AvailableTimesActivity extends FragmentActivity {



            final String TAG = "test";

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.i(TAG, "i AvailableTimesActivity");
                setContentView(R.layout.fragment_holder);

                //Hantera fragment
                FragmentManager fm = getSupportFragmentManager();
                //fragment_container är namnet på frameLayout i fragment_holder.xmll
                Fragment fragment = fm.findFragmentById(R.id.fragment_container);

                //Om det inte finns något fragment än (första gången onCreate körs), hämta nytt AvailableTimesFragment
        if(fragment == null){
        fragment = new AvailableTimesFragment();
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        }


        }

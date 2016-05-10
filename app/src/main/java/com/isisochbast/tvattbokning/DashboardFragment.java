package com.isisochbast.tvattbokning;

//I DashboardFragment kan användaren välja vad hen vill göra, se lediga tider, se sina bokningar eller logga ut

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kinvey.android.Client;

public class DashboardFragment extends Fragment {
    private Button mAvailableTimes;
    private Button mMyReservations;
    private Button mSignOut;
    private final String TAG = "test";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);


        Log.i(TAG,"DashboardFragment");

        //Behövs för att logga ut
        final Client mKinveyClient = new Client.Builder(this.getContext()).build();

        //Aktivera knapparna
        mAvailableTimes = (Button) v.findViewById(R.id.availableTimes);
        mSignOut =(Button) v.findViewById(R.id.signOut);
        mMyReservations = (Button) v.findViewById(R.id.myReservations);
        //Kommer till lediga tider
        mAvailableTimes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PickerDialogs pickerDialogs = new PickerDialogs();
                pickerDialogs.show(getFragmentManager(), "date_picker");




              /* Nu har jag ändrat så att en inte kommer till AvailableTimesFragment, istället visas en datepickerdialog i denna aktivitet, vet ej om dte är bättre att
                göra en ny aktivitet
                Intent i = new Intent(DashboardFragment.this.getContext(), AvailableTimesActivity.class);
                startActivity(i);*/
            }
        });


        //Logga ut, blir tillbakaskickad till startsidan
        mSignOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mKinveyClient.user().logout().execute();
                Toast.makeText(getContext(), "Du har loggats ut", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DashboardFragment.this.getContext(), StartActivity.class);
                startActivity(i);
            }
        });
        return v;
    }


}

package com.isisochbast.tvattbokning;

//I DashboardFragment kan användaren välja vad hen vill göra, se lediga tider, se sina bokningar eller logga ut

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends KinveyFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button availableTimes = (Button) v.findViewById(R.id.availableTimes);
        Button signOut = (Button) v.findViewById(R.id.signOut);
        Button myReservations = (Button) v.findViewById(R.id.myReservations);

        availableTimes.setOnClickListener(v1 -> {
            Intent i = new Intent(DashboardFragment.this.getContext(), AvailableTimesActivity.class);
            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
            String today = sdf.format(date.getTime());
            i.putExtra("date", today);
            startActivity(i);
        });

        myReservations.setOnClickListener(v12 -> {
            Intent i = new Intent(DashboardFragment.this.getContext(), MinaTvattiderActivity.class);
            startActivity(i);
        });

        signOut.setOnClickListener(v13 -> {
            getClient().user().logout().execute();
            Toast.makeText(getContext(), "Du har loggats ut", Toast.LENGTH_LONG).show();
            Intent i = new Intent(DashboardFragment.this.getContext(), StartActivity.class);
            startActivity(i);
        });
        return v;
    }
}

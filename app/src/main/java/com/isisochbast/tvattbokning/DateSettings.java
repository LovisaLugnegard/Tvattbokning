package com.isisochbast.tvattbokning;

/*Hanterar datum mha PIckerDialog
*/

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


class DateSettings extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final Context context;

    public DateSettings(Context context) {
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());


        final String daySelected = sdf.format(calendar.getTime());
        Intent i = new Intent(context, AvailableTimesActivity.class);
        i.putExtra("date", daySelected);
        context.startActivity(i);
    }
}

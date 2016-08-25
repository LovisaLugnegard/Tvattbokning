package com.isisochbast.tvattbokning;

/*Hanterar datum mha PIckerDialog
*/

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


class DateSettings extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final Context context;
    private final String TAG = "test";
    public DateSettings(Context context) {
        this.context = context;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        //Detta är ev en dålig datumlösning, men den får vara kvar sålänge/L
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        final String daySelected = sdf.format(calendar.getTime());
        Log.i(TAG, daySelected);


        //Vad är skillnaden på mKinveyClient.query och new Query()??
        final Client mClient = TvattbokningApp.getClient();


        Query q;
        q = new Query().equals("date", daySelected);
        final AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);
        myData.get(q, new KinveyListCallback<Tvattid>() {

            //Queryn kunde inte genomföras
            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error fetching data: " + t.getMessage());

            }

            //Lyckad query - detta borde kanske ske redan när en klickar på "lediga datum"? isf behöver det inte göras varje gång som en klickar på ett nytt datum..
            @Override
            public void onSuccess(Tvattid[] eventEntities) {
                Log.i(TAG, "i onSuccess myData" + eventEntities.length);
                //Om det inte finns några hämtade datum, spara datumet
                if (eventEntities.length == 0) {
                    Log.d(TAG, "i if onSuccess");
                    sparaTvattid(daySelected);
                }
                //Om det finns hämtade datum, gå igenom listan och kolla om någåt är samma (eg. onödigt just nu, eftersom bara det valda datumet hämtas, denna lista borde därför alltid
                //vara tom, när tider ska in kan den dock användas)
                else {
                    for (int i = 0; i < eventEntities.length; ) {
                        if (eventEntities[i].getDate().equals(daySelected)) {
                            Toast.makeText(context, "This date is taken. Choose another date.",
                                    Toast.LENGTH_SHORT).show();
                            Log.i(TAG, String.valueOf(i));
                            break;
                        } else {
                            i++;
                        }
                        sparaTvattid(daySelected);
                    }
                }

            }
        });

    }

    //metod för att spara datum i Kinvey
    private void sparaTvattid(String date) {
        Toast.makeText(context, "Selected date : " + date,
                Toast.LENGTH_SHORT).show();
        Tvattid tvattid = new Tvattid();
        tvattid.setDate(date);
        AsyncAppData<Tvattid> myevents = TvattbokningApp.getClient().appData("events", Tvattid.class);
        myevents.save(tvattid, new KinveyClientCallback<Tvattid>() {
            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "failed to save tvattid data", e);
            }

            @Override
            public void onSuccess(Tvattid r) {
                Log.d(TAG, "saved data for entity " + r.getDate());
            }
        });
    }


}

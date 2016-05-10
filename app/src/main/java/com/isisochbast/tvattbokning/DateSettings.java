package com.isisochbast.tvattbokning;

/*Hanterar datum mha PIckerDialog
*/
import android.app.DatePickerDialog;
import android.content.Context;
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

/**
 * Created by Lovisa on 2016-05-04.
 */
public class DateSettings implements DatePickerDialog.OnDateSetListener {

    Context context;
    final String TAG = "test";

    public DateSettings(Context context){

        this.context = context;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Client mKinveyClient = new Client.Builder(this.context).build();



        Calendar calendar = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        final String daySelected = sdf.format(calendar.getTime());
        Log.i(TAG, daySelected);





        Query q = mKinveyClient.query();
        q.equals("date",daySelected);
       // q.addSort("name", SortOrder.Asc);
        final AsyncAppData<Tvattid> myData = mKinveyClient.appData("events", Tvattid.class);
        myData.get(q, new KinveyListCallback<Tvattid>() {

            //Queryn kunde inte genomföras
            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error fetching data: " + t.getMessage());
            }
            //Lyckad query - detta borde kanske ske redan när en klickar på "lediga datum"? isf behöver det inte göras varje gång som en klickar på ett nytt datum..
            @Override
            public void onSuccess(Tvattid[] eventEntities) {
                    //Om det inte finns några hämtade datum, spara datumet
                    if(eventEntities == null){
                        saveDate(daySelected);
                    }
                    //Om det finns hämtade datum, gå igenom listan och kolla om någåt är samma (eg. onödigt just nu, eftersom bara det valda datumet hämtas, denna lista borde därför alltid
                    //vara tom, när tider ska in kan den dock användas)
                    else{
                        for(int i=0; i<eventEntities.length;){

                            if(eventEntities[i].getDate().equals(daySelected)){
                                Toast.makeText(context,"Your selected date is taken. Choose another date.",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            }
                            else{i++;}}

                            saveDate(daySelected);
                    }

            }
        });


    }
    //metod för att spara datum i Kinvey
    public void saveDate(String date){
        final Client mKinveyClient = new Client.Builder(this.context).build();
        Toast.makeText(context, "Selected date : " + date,
                Toast.LENGTH_SHORT).show();
        Tvattid event = new Tvattid();
        event.setDate(date);
        AsyncAppData<Tvattid> myevents = mKinveyClient.appData("events", Tvattid.class);
        myevents.save(event, new KinveyClientCallback<Tvattid>() {
            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "failed to save event data", e);
            }
            @Override
            public void onSuccess(Tvattid r) {
                Log.d(TAG, "saved data for entity "+ r.getDate());
            }
        });
    }
}

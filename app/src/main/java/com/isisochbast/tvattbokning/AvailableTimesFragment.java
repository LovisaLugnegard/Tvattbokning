package com.isisochbast.tvattbokning;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class AvailableTimesFragment extends KinveyFragment implements DatePickerDialog.OnDateSetListener {

    private RecyclerView mMinaTvattiderRecyclerView;
    private AvailableTimesFragment.TvattidAdapter mAdapter;
    List<Tvattid> mTvattider;
    private ProgressDialog mProgressBar;
    private ArrayList<Tvattid> mQueryResult;
    private boolean laddad = false;
    private ArrayList<Tvattid> mLedigaTvattider;
    public String daySelected;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d(TAG, "onATtach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        daySelected = getArguments().getString("date");

        mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setTitle(R.string.progressBar_laddar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.show();
        View view = inflater.inflate(R.layout.fragment_mina_tvattider_list, container, false);
        mMinaTvattiderRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_tvattider);
        Button buttonBytDatum = (Button) view.findViewById(R.id.button_byt_datum);

        buttonBytDatum.setOnClickListener(view1 -> {
                    PickerDialogs pickerDialogs = new PickerDialogs();
                    pickerDialogs.show(getFragmentManager(), "date_picker");
                }
        );

        /*Behöver LayoutManager för att positionera på skärmen och bestämma hur skrollning ska ske,
        RecyclerView behöver LayoutManager för att inte krascha*/
        mMinaTvattiderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TextView textViewDate = (TextView) view.findViewById(R.id.textView_title);
        String string = String.format("Lediga tvättider \n %s", daySelected);
        textViewDate.setText(string);
        mTvattider = new ArrayList<>();
        mQueryResult = new ArrayList<>();
        getAvailableTimes();
        updateUI();
        return view;
    }

    public void getAvailableTimes() {
        int t = 8;
        mLedigaTvattider = new ArrayList<>();
        while (t < 22) {
            Tvattid tvattid = new Tvattid();
            tvattid.setTime(t);
            tvattid.setDate(daySelected);
            mLedigaTvattider.add(tvattid);
            t = t + 2;
        }

        laddad = false;
        Client mClient = getClient();
        AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);
        Query q = new Query().equals("date", daySelected);
        myData.get(q, new KinveyListCallback<Tvattid>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(Tvattid[] result) {

                ArrayList<Tvattid> resultList = new ArrayList<>(Arrays.asList(result));

                mQueryResult.clear();
                mQueryResult.addAll(resultList);
                mLedigaTvattider.removeIf(inResultList);
                setQueryResult(mLedigaTvattider);
                mProgressBar.hide();
                laddad = true;
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");
            }

            final Predicate<Tvattid> inResultList = x -> {

                for (int i = 0; i < mQueryResult.size(); i++) {
                    if (x.getTime() == mQueryResult.get(i).getTime()) {
                        return true;
                    }
                }
                return false;
            };
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "MTF onREsume");
        //updateUI();
    }

    public void setQueryResult(ArrayList<Tvattid> tvattider) {
        mTvattider = tvattider;
    }

    private void updateUI() {
        if (laddad) {
            if (mAdapter == null) {
                mAdapter = new AvailableTimesFragment.TvattidAdapter(mTvattider);
                mMinaTvattiderRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setTvattider(mTvattider);
                mAdapter.notifyDataSetChanged();
            }
        }
        //Detta else är för att vi måste vänta på att alla tvättider har laddats ner, annars kommer inte listan att uppdateras i appen
        else {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                Log.i(TAG, "MTF run" + laddad);
                updateUI();
            }, 10);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());

        daySelected = sdf.format(calendar.getTime());
    }

    private class TvattidHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Tvattid mTvattid;
        private TextView mTimeTextView;
        private Button mBokaButton;

        TvattidHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_lediga_tvattider_time);
            mBokaButton = (Button) itemView.findViewById(R.id.list_item_lediga_tvattider_button);
            mBokaButton.setOnClickListener(this);
        }

        //bindTvattid är till för att onBindViewHolder bara ska kunna anropa denna metod istället för att göra annat,
        //dvs adaptern gör inget, allt sker i Holdern
        void bindTvattid(Tvattid tvattid) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            mTvattid = tvattid;
            int time = mTvattid.getTime();
            Calendar startTime = new GregorianCalendar();
            startTime.set(Calendar.HOUR_OF_DAY, time);
            startTime.set(Calendar.MINUTE, 0);
            Calendar endTime = new GregorianCalendar();
            endTime.set(Calendar.HOUR_OF_DAY, time + 2);
            endTime.set(Calendar.MINUTE, 0);
            final String startTimeSelected = sdf.format(startTime.getTime());
            final String endTimeSelected = sdf.format(endTime.getTime());
            mTimeTextView.setText(startTimeSelected + "-" + endTimeSelected);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Vill du boka tvättiden?");
            alertDialogBuilder.setPositiveButton("Ja", (arg0, arg1) -> {
                String eventDate = mTvattid.getDate();
                int time = mTvattid.getTime();
                bokaTvattid(eventDate, time);
            });

            alertDialogBuilder.setNegativeButton("Nej", (dialog, which) -> {
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void bokaTvattid(String date, int time) {
        Toast.makeText(getContext(), "Du har bokat: " + date,
                Toast.LENGTH_SHORT).show();
        Tvattid tvattid = new Tvattid();
        tvattid.setDate(date);
        tvattid.setTime(time);
        AsyncAppData<Tvattid> myevents = getClient().appData("events", Tvattid.class);
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

    //Adaptern - det är med denna som RecyclerView "pratar" när det behövs en ny ViewHolder, RecyclerView vet alltså ingenting
    //om några Tvattid-objekt (men det gör adaptern)
    private class TvattidAdapter extends RecyclerView.Adapter<TvattidHolder> {
        private List<Tvattid> mTvattider;

        private TvattidAdapter(List<Tvattid> tvattider) {
            mTvattider = tvattider;
        }

        //Anropas av RecyclerView när det behövs en ny View, här skapas en ny View som sen packeteras in i
        //en ViewHolder
        @Override
        public AvailableTimesFragment.TvattidHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_lediga_tvattider, parent, false);
            return new AvailableTimesFragment.TvattidHolder(view);
        }

        //Kopplar ViewHolders View till ett modellobjekt
        @Override
        public void onBindViewHolder(AvailableTimesFragment.TvattidHolder holder, int position) {
            Tvattid tvattid = mTvattider.get(position);
            holder.bindTvattid(tvattid);
        }

        @Override
        public int getItemCount() {
            return mTvattider.size();
        }

        private void setTvattider(List<Tvattid> tvattider) {
            mTvattider = tvattider;
        }
    }
}




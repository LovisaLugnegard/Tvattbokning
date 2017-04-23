package com.isisochbast.tvattbokning;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;


/**
 * Ett fargment för att hålla alla användarens bokade tvättider
 * Tvättiderna kommer att visas som en lista som kan skrollas ner (kanske är detta överdrivet, hur många tvättider kan en boka på en gång?)
 * För att göra detta används RecyclerView, då behöver vi inte skapa nya textViews till varje bokning, utan kan återanvända de som visas på skärmen
 */
public class MinaTvattiderFragment extends KinveyFragment {

    static Calendar cal = Calendar.getInstance();
    static SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());

    private static final Predicate<Tvattid> isBefore = x -> {
        if (x.getDate() == null) {
            return false;
        } else {
            try {
                cal.setTime(sdf.parse(x.getDate()));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_WEEK, -1);
                return cal.before(c);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    };

    private RecyclerView mMinaTvattiderRecyclerView;
    private TvattidAdapter mAdapter;
    List<Tvattid> mTvattider;
    ProgressDialog mProgressBar;
    private ArrayList<Tvattid> mMinaTvattider;
    private boolean laddad = false;
    private TextView mIngaBokadeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setIndeterminate(true);
        mProgressBar.setTitle(getString(R.string.progressBar_laddar_dina));
        mProgressBar.show();
        View view = inflater.inflate(R.layout.fragment_mina_tvattider_list, container, false);
        mMinaTvattiderRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_tvattider);
        Button mBytaDatumButton = (Button) view.findViewById(R.id.button_byt_datum);
        mBytaDatumButton.setVisibility(View.INVISIBLE);

        /*Behöver LayoutManager för att positionera på skärmen och bestämma hur skrollning ska ske,
        RecyclerView behöver LayoutManager för att inte krascha*/
        mMinaTvattiderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TextView titleTextView = (TextView) view.findViewById(R.id.textView_title);
        titleTextView.setText(R.string.text_view_dina_bokade_tt);
        mIngaBokadeTextView = (TextView) view.findViewById(R.id.textView_tvattider_list);
        mIngaBokadeTextView.setVisibility(View.GONE);
        mTvattider = new ArrayList<>();
        mMinaTvattider = new ArrayList<>();

        getMinaTvattider();
        updateUI();
        return view;
    }

    private void getMinaTvattider() {

        laddad = false;
        Log.d(TAG, "TL getMinaTvattider");
        Client mClient = TvattbokningApp.getClient();
        AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);
        Query q = new Query().equals("_acl.creator", mClient.user().getId());
        myData.get(q, new KinveyListCallback<Tvattid>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(Tvattid[] result) {
                Log.d(TAG, "TL on Success" + result.length);

                ArrayList<Tvattid> resultList = new ArrayList<>(Arrays.asList(result));
                int size = resultList.size();
                Log.d(TAG, "size resultlist" + String.valueOf(size));

                if (size != 0) {
                    resultList.removeIf(isBefore);
                    mMinaTvattider.clear();
                    mMinaTvattider.addAll(resultList);
                    setMinaTvattider(mMinaTvattider);
                    mProgressBar.hide();
                    laddad = true;
                } else {
                    mIngaBokadeTextView.setText(R.string.inga_bokade_tider);
                }
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "MTF onREsume");
        //updateUI();
    }

    public void setMinaTvattider(ArrayList<Tvattid> tvattider) {
        mTvattider = tvattider;
    }

    private void updateUI() {
        if (laddad) {
            if (mTvattider.size() == 0) {
                mIngaBokadeTextView.setText(R.string.inga_bokade_tider);
                mIngaBokadeTextView.setVisibility(View.VISIBLE);
            }
            if (mAdapter == null) {
                mAdapter = new TvattidAdapter(mTvattider);
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

    //ViewHolder (en inre klass), OBS!!! just nu måste itemView vara en TextView!
    private class TvattidHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Tvattid mTvattid;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private Button mAvbokaButton;

        TvattidHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_mina_tvattider_time);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_mina_tvattider_date);
            mAvbokaButton = (Button) itemView.findViewById(R.id.list_item_button);
            mAvbokaButton.setText(R.string.button_avboka);
            mAvbokaButton.setOnClickListener(this);
        }

        //bindTvattid är till för att onBindViewHolder bara ska kunna anropa denna metod istället för att göra annat,
        //dvs adaptern gör inget, allt sker i Holdern
        void bindTvattid(Tvattid tvattid) {
            mTvattid = tvattid;
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
            mDateTextView.setText(mTvattid.getDate());
            mTimeTextView.setText(startTimeSelected + "-" + endTimeSelected);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Vill du avboka tvättiden?");
            alertDialogBuilder.setPositiveButton("Ja", (arg0, arg1) -> {
                String eventId = mTvattid.getId();
                avbokaTvattid(eventId);
            });

            alertDialogBuilder.setNegativeButton("Nej", (dialog, which) -> {
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void avbokaTvattid(String eventId) {
        AsyncAppData<Tvattid> myevents = getClient().appData("events", Tvattid.class);
        myevents.delete(eventId, new KinveyDeleteCallback() {
            @Override
            public void onSuccess(KinveyDeleteResponse response) {
                getMinaTvattider();
                updateUI();
            }

            public void onFailure(Throwable error) {
                Log.e(TAG, "failed to delete ", error);
            }
        });

    }

    //Adaptern - det är med denna som RecyclerView "pratar" när det behövs en ny ViewHolder, RecyclerView vet alltså ingenting
    //om några Tvattid-objekt (men det gör adaptern)
    private class TvattidAdapter extends RecyclerView.Adapter<TvattidHolder> {
        private List<Tvattid> mTvattider;

        TvattidAdapter(List<Tvattid> tvattider) {
            mTvattider = tvattider;
        }

        //Anropas av RecyclerView när det behövs en ny View, här skapas en ny View som sen packeteras in i
        //en ViewHolder
        @Override
        public TvattidHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            Log.d(TAG, "MTF TvattidHolder onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_mina_tvattider, parent, false);
            return new TvattidHolder(view);
        }

        //Kopplar ViewHolders View till ett modellobjekt
        @Override
        public void onBindViewHolder(TvattidHolder holder, int position) {
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

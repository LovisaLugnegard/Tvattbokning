package com.isisochbast.tvattbokning;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.isisochbast.tvattbokning.Model.Tvattid;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ett fargment för att hålla alla användarens bokade tvättider
 * Tvättiderna kommer att visas som en lista som kan skrollas ner (kanske är detta överdrivet, hur många tvättider kan en boka på en gång?)
 * För att göra detta används RecyclerView, då behöver vi inte skapa nya textViews till varje bokning, utan kan återanvända de som visas på skärmen
 */
public class MinaTvattiderFragment extends KinveyFragment {

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

        Log.d(TAG, "i MinaTvattiderFragment");
        View view = inflater.inflate(R.layout.fragment_mina_tvattider_list, container, false);
        mMinaTvattiderRecyclerView = (RecyclerView) view.findViewById(R.id.mina_tvattider_recycler_view);


        /*Behöver LayoutManager för att positionera på skärmen och bestämma hur skrollning ska ske,
        RecyclerView behöver LayouManager för att inte krascha*/
        mMinaTvattiderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngaBokadeTextView = (TextView) view.findViewById(R.id.textView_inga_bokningar);
        mIngaBokadeTextView.setVisibility(View.GONE);
        mTvattider = new ArrayList<>();
        mMinaTvattider = new ArrayList<>();

        blablabla();
        updateUI();
        return view;
    }


    public void blablabla() {

        laddad = false;
        Log.d(TAG, "TL blablabla");

        //mProgressBar.show();


        // mMinaTvattider.clear();
        //  Log.i(TAG, "TL getMinaTvattider mMinaTvattider: " + mMinaTvattider.size());

        Client mClient = TvattbokningApp.getClient();

//        Log.i(TAG, "" + mClient.user().isUserLoggedIn());

        AsyncAppData<Tvattid> myData = mClient.appData("events", Tvattid.class);


        Query q = new Query().equals("_acl.creator", mClient.user().getId());

        myData.get(q, new KinveyListCallback<Tvattid>() {

            @Override

            public void onSuccess(Tvattid[] result) {
                //  mMinaTvattider = result;
                Log.d(TAG, "TL on Success" + result.length);

                mMinaTvattider.clear();
                mMinaTvattider.addAll(Arrays.asList(result));

                Log.d(TAG, "TL MT storlek: " + mMinaTvattider.size());
                //MinaTvattiderFragment m = new MinaTvattiderFragment();
                setMinaTvattidervattider(mMinaTvattider);
                mProgressBar.hide();
                laddad = true;


//got entities created by the current user

            }


            @Override

            public void onFailure(Throwable error) {
                Log.d(TAG, "TL onFail");

//something went wrong!

            }

        });

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "MTF onREsume");
        //updateUI();
    }

    public void setMinaTvattidervattider(ArrayList<Tvattid> tvattider) {

        mTvattider = tvattider;
        Log.i(TAG, "setTvattider" + tvattider.size());

    }

    private void updateUI() {

        Log.d(TAG, "MTF updateUI");
        // blablabla();


        // TvattidLab tvattidLab = TvattidLab.get(getActivity());
      /*  List<Tvattid> tvattider = tvattidLab.getMinaTvattider();
        mTvattider =*/ //tvattidLab.getMinaTvattider();
        //  mTvattider = new ArrayList<Tvattid>();
      /*  ArrayList<Tvattid> tvattider = new ArrayList<Tvattid>();
      /*  mTvattider = new ArrayList<Tvattid>();
        DownloadMinaTvattider downloadMinaTvattider = new DownloadMinaTvattider(getContext());
        downloadMinaTvattider.execute();*/

//        Log.i(TAG, "" + mTvattider.size());


        if (laddad) {

            if (mTvattider.size() == 0) {
                mIngaBokadeTextView.setVisibility(View.VISIBLE);
            }


            Log.i(TAG, "MTF if..");
            if (mAdapter == null) {
                //Log.i(TAG, "UpdateUI if..");
                mAdapter = new TvattidAdapter(mTvattider);
                mMinaTvattiderRecyclerView.setAdapter(mAdapter);
            } else {
                Log.i(TAG, "updateUI else.." + mTvattider.size());

                mAdapter.setTvattider(mTvattider);
                mAdapter.notifyDataSetChanged();
            }
        }
        //Detta else är för att vi måste vänta på att alla tvättider har laddats ner, annars kommer inte listan att uppdateras i appen
        else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "MTF run" + laddad);
                    updateUI();
                }
            }, 10);
        }


    }

    //ViewHolder (en inre klass), OBS!!! just nu måste itemView vara en TextView!
    private class TvattidHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Tvattid mTvattid;

        private TextView mDateTextView;
        private Button mAvbokaButton;

        public TvattidHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "MTF TvattidHolder");
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_mina_tvattider_date);
            mAvbokaButton = (Button) itemView.findViewById(R.id.list_item_avboka_button);
            mAvbokaButton.setOnClickListener(this);
        }

        //bindTvattid är till för att onBindViewHolder bara ska kunna anropa denna metod istället för att göra annat,
        //dvs adaptern gör inget, allt sker i Holdern
        public void bindTvattid(Tvattid tvattid) {
            Log.d(TAG, "MTF bindTvattid");
            mTvattid = tvattid;
            mDateTextView.setText(mTvattid.getDate());
        }

        @Override
        public void onClick(View view) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Vill du avboka tvättiden?");

            alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String eventId = mTvattid.getId();
                    avbokaTvattid(eventId);
                }
            });

            alertDialogBuilder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
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

                Log.v(TAG, "deleted successfully");
                blablabla();
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

        public TvattidAdapter(List<Tvattid> tvattider) {
            Log.d(TAG, "MTF TvattidAdapter");
            mTvattider = tvattider;
        }

        //Anropas av RecyclerView när det behövs en ny View, här skapas en ny View som sen packeteras in i
        //en ViewHolder
        @Override
        public TvattidHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            Log.d(TAG, "MTF TvattidHolder onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //Hämtar View
            View view = layoutInflater.inflate(R.layout.list_item_mina_tvattider, parent, false);
            return new TvattidHolder(view);
        }

        //Kopplar ViewHolders View till ett modellobjekt
        //Position = index för tvättiden i en array
        @Override
        public void onBindViewHolder(TvattidHolder holder, int position) {
            Log.d(TAG, "MTF onBindViewHolder");
            Tvattid tvattid = mTvattider.get(position);
            holder.bindTvattid(tvattid);
        }


        //getItemCount måste implementeras för att klassen ska fungera
        @Override
        public int getItemCount() {
            Log.d(TAG, "MTF getItemCount");
            return mTvattider.size();
        }

        public void setTvattider(List<Tvattid> tvattider) {
            mTvattider = tvattider;
        }

    }

}

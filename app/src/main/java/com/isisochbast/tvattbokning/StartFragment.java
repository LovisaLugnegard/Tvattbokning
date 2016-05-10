package com.isisochbast.tvattbokning;

/*
* Härifrån visas startsidan, här kan användaren logga in samt hämta nytt lösenord*/

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class StartFragment extends Fragment {
    public String TAG = "test";


    private Button mLoginButton;
    private Button mForgotPasswordButton;
    private EditText mEditUserName;
    private EditText mEditPassword;
    private Button mLogoutButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        final Client mKinveyClient = new Client.Builder(this.getContext()).build();

        Log.i(TAG, "onCreateView");
        mLoginButton = (Button) v.findViewById(R.id.button_login);
        mForgotPasswordButton = (Button) v.findViewById(R.id.button_forgot_password);
        mLogoutButton = (Button) v.findViewById(R.id.button_logout);
        mEditUserName = (EditText) v.findViewById(R.id.username);
        mEditPassword = (EditText) v.findViewById(R.id.password);









      /* lite kod för att skapa en ny tvättid, ev användbar senare i bokningsaktivitet
       Tvattid event = new Tvattid();
        event.setDate("idag");
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
*/


        //Klicka på logga in, loggar in i kinvey, om det är rätt lösenord; till översikt av konto(dashboard), om fel lösenord; toast
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick");

                mKinveyClient.user().login(mEditUserName.getText().toString(), mEditPassword.getText().toString(), new KinveyUserCallback() {

                    @Override
                    public void onFailure(Throwable t) {

                        CharSequence text = "Wrong username or password.";
                        Toast.makeText(StartFragment.this.getActivity(),text, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(User u) {
                        //Log.i("i onSuccess");
                        CharSequence text = "Welcome back," + u.getUsername() + ".";
                        Toast.makeText(StartFragment.this.getActivity(), text, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "i onSuccess");
                        Intent i = new Intent(StartFragment.this.getActivity(), DashboardActivity.class);
                        startActivity(i);

                        /*CharSequence text = "Welcome back," + u.getUsername() + ".";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();*/
                    }
                });

            }

        });
        Log.i(TAG, "efter login");

        mLogoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mKinveyClient.user().logout().execute();
            }

        });

        return v;
    }





}

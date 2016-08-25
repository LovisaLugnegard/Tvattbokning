package com.isisochbast.tvattbokning;

/*
* Härifrån visas startsidan, här kan användaren logga in samt hämta nytt lösenord*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class StartFragment extends KinveyFragment {

    private EditText mEditUserName;
    private EditText mEditPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "SF onCreate");

    }

    //on* är för att se vad som händer, ta bort dem senare!
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "SF onStart called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "SF onPause called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "SF onResume called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SF onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SF onDestroy called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        //Lite oklart hur denna rad funkar men den behövs, annars kraschar appen
        //  ((TvattbokningApp) getActivity().getApplicationContext()).getClient();


        Log.i(TAG, "SF onCreateView");
        Button loginButton = (Button) v.findViewById(R.id.button_login);
        Button forgotPasswordButton = (Button) v.findViewById(R.id.button_forgot_password);
       // Button logoutButton = (Button) v.findViewById(R.id.button_logout);
        mEditUserName = (EditText) v.findViewById(R.id.username);
        mEditPassword = (EditText) v.findViewById(R.id.password);




        //Klicka på logga in, loggar in i kinvey, om det är rätt lösenord; till översikt av konto(dashboard), om fel lösenord; toast
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getClient().user().login(mEditUserName.getText().toString(), mEditPassword.getText().toString(), new KinveyUserCallback() {


                    @Override
                    public void onFailure(Throwable t) {

                        CharSequence text = "Wrong username or password.";
                        Toast.makeText(StartFragment.this.getActivity(), text, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(User u) {
                        CharSequence text = "Welcome back," + u.getUsername() + ".";
                        Toast.makeText(StartFragment.this.getActivity(), text, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(StartFragment.this.getActivity(), DashboardActivity.class);
                        startActivity(i);

                    }
                });

            }

        });

       /* logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().user().logout().execute();
                Log.d(TAG, "SF log out");
            }

        });*/

        return v;
    }

}

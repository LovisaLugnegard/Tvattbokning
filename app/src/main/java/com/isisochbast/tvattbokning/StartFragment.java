package com.isisochbast.tvattbokning;

/*
* Härifrån visas startsidan, här kan användaren logga in samt hämta nytt lösenord*/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.android.callback.KinveyUserManagementCallback;
import com.kinvey.java.User;

public class StartFragment extends KinveyFragment implements View.OnClickListener {

    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    Client client = getClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Button buttonLogin = (Button) v.findViewById(R.id.button_login);
        Button buttonNewPassword = (Button) v.findViewById(R.id.button_forgot_password);
        mEditTextUserName = (EditText) v.findViewById(R.id.username);
        mEditTextPassword = (EditText) v.findViewById(R.id.password);

        //Klicka på logga in, loggar in i kinvey, om det är rätt lösenord; till översikt av konto(dashboard),
        // om fel lösenord; toast
        buttonLogin.setOnClickListener(v1 -> getClient().user().login(mEditTextUserName.getText().toString(),
                mEditTextPassword.getText().toString(), new KinveyUserCallback() {

                    @Override
                    public void onFailure(Throwable t) {
                        CharSequence text = getString(R.string.wrong_username_PW);
                        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(User u) {
                        CharSequence text = "Välkommen " + u.getUsername() + ".";
                        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), DashboardActivity.class);
                        startActivity(i);
                    }
                }));
        buttonNewPassword.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.alert_DB_new_PW);
        alertDialogBuilder.setPositiveButton(R.string.yes, (arg0, arg1) -> resetPW());

        alertDialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void resetPW() {
        client.user().resetPassword(mEditTextUserName.getText().toString(), new KinveyUserManagementCallback() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), R.string.new_PW_sent, Toast.LENGTH_LONG).show();
            }

            public void onFailure(Throwable e) {
                Toast.makeText(getContext(), R.string.no_user_name, Toast.LENGTH_LONG).show();
            }
        });
    }
}



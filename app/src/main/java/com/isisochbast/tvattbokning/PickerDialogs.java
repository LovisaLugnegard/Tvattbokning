package com.isisochbast.tvattbokning;

/*
    Hanterar kalerndern som visas tillsammans med DateSetting*/

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class PickerDialogs extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState) {

        DateSettings dateSettings = new DateSettings(getActivity());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;

        dialog = new DatePickerDialog(getContext(), dateSettings, year, month, day);


        return dialog;
    }
}

package com.derekpoon.crimereporter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by derekpoon on 26/02/2018.
 */

public class DatePickerFragment extends DialogFragment {

    //builds an AlertDialog with title and OK button
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //inflate the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        return new AlertDialog.Builder(getActivity())
                //set it on the dialog
                .setView(v)
        .setTitle(R.string.date_picker_title)
        .setPositiveButton(android.R.string.ok, null)
        .create();
    }
}

package com.android.flighttime.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.flighttime.R;
import com.android.flighttime.listener.CityChangeListener;

import java.util.Calendar;

/**
 * Created by OldMan on 10.05.2016.
 */
public class CityIndexDialog extends android.support.v4.app.DialogFragment {
    private static CityChangeListener callback;

    public static CityIndexDialog newInstance(CityChangeListener listener) {
        final CityIndexDialog frag = new CityIndexDialog();
        callback = listener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View inflator = inflater.inflate(R.layout.dialog_change_mission, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflator)
                // Add action buttons
                .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CityIndexDialog.this.getDialog().cancel();
                    }
                }).setTitle(R.string.enter_travel_city).setCancelable(true);



        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    EditText addressText=(EditText)d.findViewById(R.id.autocomplete_places);
                    String address=addressText.getText().toString();
                    if(!address.isEmpty()) {
                        callback.onChangeCity(address);
                        dismiss();
                    }
                    else {
                        addressText.requestFocus();
                        addressText.setError(getString(R.string.city_error));
                    }
                }
            });
        }
    }
}

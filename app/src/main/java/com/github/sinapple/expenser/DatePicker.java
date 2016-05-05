package com.github.sinapple.expenser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pila2 on 26.04.2016.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // determine the current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // create DatePickerDialog and return
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle(R.string.set_title_for_date_picker);
        return picker;
    }

    //start date picker
    @Override
    public void onStart() {
        super.onStart();
        // add text to button
        Button nButton = ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(R.string.set_date_picker);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        //initialize textView for date
        TextView tv = (TextView) getActivity().findViewById(R.id.tv_date);
        //set format date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sDate = sdf.format(new Date(System.currentTimeMillis()));
        //set date on textView
        tv.setText(day + "." + (month+1) + "." + year + " " + sDate);
    }
}

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

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Choose date");
        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton = ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Set");
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        TextView tv = (TextView) getActivity().findViewById(R.id.tv_date);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sDate = sdf.format(new Date(System.currentTimeMillis()));
        tv.setText(day + "." + month + "." + year + " " + sDate);

    }
}

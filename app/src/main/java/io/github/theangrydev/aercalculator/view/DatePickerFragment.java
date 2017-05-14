package io.github.theangrydev.aercalculator.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import org.joda.time.LocalDate;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String MAX_DATE = "maxDate";
    private static final String INITIAL_DATE = "initialDate";
    private static final String ACTION = "action";

    private DatePickerAction action;
    private LocalDate maxDate;
    private LocalDate initialDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        restoreArguments();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return honeyCombAndAboveDialog();
        } else {
            return belowHoneyCombDialog();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        action.onDateSet(new LocalDate(year, month + 1, dayOfMonth));
    }

    public void setArguments(DatePickerAction action, LocalDate initialDate, LocalDate maxDate) {
        Bundle arguments = new Bundle(3);
        arguments.putSerializable(ACTION, action);
        arguments.putSerializable(INITIAL_DATE, initialDate);
        arguments.putSerializable(MAX_DATE, maxDate);
        setArguments(arguments);
    }

    private void restoreArguments() {
        Bundle arguments = getArguments();
        action = (DatePickerAction) arguments.getSerializable(ACTION);
        maxDate = (LocalDate) arguments.getSerializable(MAX_DATE);
        initialDate = (LocalDate) arguments.getSerializable(INITIAL_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private Dialog honeyCombAndAboveDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, initialDate.getYear(), initialDate.getMonthOfYear(), initialDate.getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.toDateTimeAtCurrentTime().getMillis());
        return datePickerDialog;
    }

    private Dialog belowHoneyCombDialog() {
        return new DatePickerDialog(getActivity(), this, initialDate.getYear(), initialDate.getMonthOfYear(), initialDate.getDayOfMonth()) {

            @Override
            public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate dateTime = new LocalDate(year, month + 1, dayOfMonth);
                if (dateTime.isAfter(maxDate)) {
                    view.updateDate(maxDate.getYear(), maxDate.getMonthOfYear() - 1, maxDate.getDayOfMonth());
                }
            }
        };
    }
}

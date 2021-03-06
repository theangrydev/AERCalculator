package io.github.theangrydev.aercalculator.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
        return datePickerDialog();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        AERCalculatorActivity activity = (AERCalculatorActivity) getActivity();
        action.onDateSet(activity.presenter(), new LocalDate(year, month + 1, dayOfMonth));
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

    private Dialog datePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, initialDate.getYear(), initialDate.getMonthOfYear(), initialDate.getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.toDateTimeAtCurrentTime().getMillis());
        return datePickerDialog;
    }
}

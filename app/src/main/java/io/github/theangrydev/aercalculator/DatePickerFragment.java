package io.github.theangrydev.aercalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import org.joda.time.LocalDate;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Button button;
    private LocalDate maxDate;
    private LocalDate initialDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return honeyCombAndAboveDialog();
        } else {
            return belowHoneyCombDialog();
        }
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
                LocalDate dateTime = new LocalDate(year, month, dayOfMonth);
                if (dateTime.isAfter(maxDate)) {
                    view.updateDate(maxDate.getYear(), maxDate.getMonthOfYear() - 1, maxDate.getDayOfMonth());
                }
            }
        };
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        button.setText(formatDate(year, month, dayOfMonth));
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return getString(R.string.date_format_parts, dayOfMonth, month + 1, year);
    }
}

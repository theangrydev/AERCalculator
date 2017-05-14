package io.github.theangrydev.aercalculator;

import android.support.v4.app.FragmentActivity;

import java.io.Serializable;

public interface DatePickerAction extends Serializable {
    void onDateSet(FragmentActivity activity, int year, int month, int dayOfMonth);
}

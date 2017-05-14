package io.github.theangrydev.aercalculator.view;

import org.joda.time.LocalDate;

import java.io.Serializable;

public interface DatePickerAction extends Serializable {
    void onDateSet(LocalDate date);
}

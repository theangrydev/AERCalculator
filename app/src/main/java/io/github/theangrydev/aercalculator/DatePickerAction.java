package io.github.theangrydev.aercalculator;

import org.joda.time.LocalDate;

import java.io.Serializable;

public interface DatePickerAction extends Serializable {
    void onDateSet(LocalDate date);
}

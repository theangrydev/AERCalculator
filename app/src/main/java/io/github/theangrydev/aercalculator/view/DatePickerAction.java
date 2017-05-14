package io.github.theangrydev.aercalculator.view;

import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import org.joda.time.LocalDate;

import java.io.Serializable;

public interface DatePickerAction extends Serializable {
    void onDateSet(AERCalculatorPresenter presenter, LocalDate date);
}

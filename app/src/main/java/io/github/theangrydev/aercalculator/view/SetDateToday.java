package io.github.theangrydev.aercalculator.view;

import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import org.joda.time.LocalDate;

public class SetDateToday implements DatePickerAction {
    @Override
    public void onDateSet(AERCalculatorPresenter presenter, LocalDate date) {
        presenter.setDateToday(date);
    }
}

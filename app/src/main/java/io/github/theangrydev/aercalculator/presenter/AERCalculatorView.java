package io.github.theangrydev.aercalculator.presenter;

import io.github.theangrydev.aercalculator.Contribution;
import io.github.theangrydev.aercalculator.DatePickerAction;
import org.joda.time.LocalDate;

import java.util.List;

public interface AERCalculatorView {
    void displayContributions(List<Contribution> contributions);
    void displayAER(double aer);
    void displayUnknownAER();
    void displayDatePicker(DatePickerAction action, LocalDate initialDate, LocalDate maxDate);
    void displayTodayDate(LocalDate todayDate);
}

package io.github.theangrydev.aercalculator.presenter;

import io.github.theangrydev.aercalculator.model.Contribution;
import io.github.theangrydev.aercalculator.view.DatePickerAction;
import org.joda.time.LocalDate;

import java.util.List;

public interface AERCalculatorView {
    void displayContributions(List<Contribution> contributions);
    void selectContribution(int selectedIndex);
    void displayAER(double aer);
    void displayUnknownAER();
    void displayDatePicker(DatePickerAction action, LocalDate initialDate, LocalDate maxDate);
    void displayTodayDate(LocalDate todayDate);
}

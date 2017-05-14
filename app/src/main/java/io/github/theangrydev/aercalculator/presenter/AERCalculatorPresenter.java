package io.github.theangrydev.aercalculator.presenter;

import io.github.theangrydev.aercalculator.AERCalculator;
import io.github.theangrydev.aercalculator.Contribution;
import io.github.theangrydev.aercalculator.DatePickerAction;
import io.github.theangrydev.aercalculator.UnknownAERException;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static io.github.theangrydev.aercalculator.Contribution.emptyContribution;

public class AERCalculatorPresenter {
    private static final LocalDate NO_MAX_DATE = new LocalDate(9999, 1, 1);

    private final AERCalculator aerCalculator = new AERCalculator();
    private final AERCalculatorView view;

    private LocalDate dateToday = LocalDate.now();
    private double valueToday;
    private List<Contribution> contributions = new ArrayList<>();

    public AERCalculatorPresenter(AERCalculatorView view) {
        this.view = view;
    }

    public void initialise() {
        showTodayDate();
        addContribution();
    }

    public void computeAER() {
        try {
            double aer = aerCalculator.computeAER(dateToday, valueToday, contributions);
            view.displayAER(aer);
        } catch (UnknownAERException e) {
            view.displayUnknownAER();
        }
    }

    public void addContribution() {
        contributions.add(emptyContribution());
        view.displayContributions(contributions);
    }

    public void showTodayDatePicker() {
        view.displayDatePicker(new DatePickerAction() {
            @Override
            public void onDateSet(LocalDate date) {
                dateToday = date;
                showTodayDate();
            }
        }, dateToday, NO_MAX_DATE);
    }

    public void showContributionDatePicker(final int contributionIndex) {
        LocalDate maxDate = dateToday.minusDays(1);
        view.displayDatePicker(new DatePickerAction() {
            @Override
            public void onDateSet(LocalDate date) {
                setContributionDate(contributionIndex, date);
                view.displayContributions(contributions);
            }
        }, maxDate, maxDate);
    }

    public void setContributionAmount(int index, String amount) {
        contributions.set(index, contributions.get(index).withAmount(parseDouble(amount)));
    }

    public void setTodayValue(String value) {
        valueToday = parseDouble(value);
    }

    private void setContributionDate(int index, LocalDate date) {
        contributions.set(index, contributions.get(index).withDate(date));
    }

    private void showTodayDate() {
        view.displayTodayDate(dateToday);
    }

    private double parseDouble(String amount) {
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

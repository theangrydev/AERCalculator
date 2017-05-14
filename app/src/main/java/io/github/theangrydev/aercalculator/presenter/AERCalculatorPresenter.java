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
    private Double valueToday;
    private List<Contribution> contributions = new ArrayList<>();

    public AERCalculatorPresenter(AERCalculatorView view) {
        this.view = view;
    }

    public void initialise() {
        showTodayDate();
        addContribution();
    }

    public void computeAER() {
        if (valueToday == null) {
            view.displayUnknownAER();
            return;
        }
        List<Contribution> filledInContributions = filledInContributions();
        if (filledInContributions.isEmpty()) {
            view.displayUnknownAER();
            return;
        }
        contributions = filledInContributions;
        view.displayContributions(this.contributions);
        try {
            double aer = aerCalculator.computeAER(dateToday, valueToday, this.contributions);
            view.displayAER(aer);
        } catch (UnknownAERException e) {
            view.displayUnknownAER();
        }
    }

    public void addContribution() {
        contributions.add(emptyContribution());
        view.displayContributions(contributions);
        view.scrollToBottomOfContributions();
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
        Contribution contribution = contributions.get(index);
        contributions.set(index, contribution.withAmount(parseDouble(amount)));
    }

    public void setTodayValue(String value) {
        if (value.isEmpty()) {
            valueToday = null;
        } else {
            valueToday = parseDouble(value);
        }
    }

    private void setContributionDate(int index, LocalDate date) {
        Contribution contribution = contributions.get(index);
        contributions.set(index, contribution.withDate(date));
    }

    private void showTodayDate() {
        view.displayTodayDate(dateToday);
    }

    private Double parseDouble(String amount) {
        if (amount.isEmpty()) {
            return null;
        } else {
            return Double.parseDouble(amount);
        }
    }

    private List<Contribution> filledInContributions() {
        List<Contribution> filledIn = new ArrayList<>();
        for (Contribution contribution : contributions) {
            if (contribution.hasAmount() && contribution.hasDate()) {
                filledIn.add(contribution);
            }
        }
        return filledIn;
    }
}

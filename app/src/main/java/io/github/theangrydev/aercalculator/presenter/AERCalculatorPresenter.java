package io.github.theangrydev.aercalculator.presenter;

import io.github.theangrydev.aercalculator.model.AERCalculator;
import io.github.theangrydev.aercalculator.model.Portfolio;
import io.github.theangrydev.aercalculator.model.UnknownAERException;
import io.github.theangrydev.aercalculator.view.DatePickerAction;
import org.joda.time.LocalDate;

import static io.github.theangrydev.aercalculator.model.Portfolio.emptyPortfolio;

public class AERCalculatorPresenter {
    private static final LocalDate NO_MAX_DATE = new LocalDate(9999, 1, 1);

    private final AERCalculator aerCalculator = new AERCalculator();
    private final AERCalculatorView view;

    private Portfolio portfolio = emptyPortfolio();

    public AERCalculatorPresenter(AERCalculatorView view) {
        this.view = view;
    }

    public void initialise() {
        showTodayDate();
        addContribution();
    }

    public void computeAER() {
        if (portfolio.hasUnknownValueToday()) {
            view.displayUnknownAER();
            return;
        }
        if (portfolio.allContributionsAreIncomplete()) {
            view.displayUnknownAER();
            return;
        }
        portfolio.removeIncompleteContributions();
        view.displayContributions(portfolio.contributions());
        try {
            double aer = aerCalculator.computeAER(portfolio.dateToday(), portfolio.valueToday(), portfolio.contributions());
            view.displayAER(aer);
        } catch (UnknownAERException e) {
            view.displayUnknownAER();
        }
    }

    public void addContribution() {
        portfolio.addEmptyContribution();
        view.displayContributions(portfolio.contributions());
        view.scrollToBottomOfContributions();
    }

    public void showTodayDatePicker() {
        view.displayDatePicker(new DatePickerAction() {
            @Override
            public void onDateSet(LocalDate date) {
                portfolio.setDateToday(date);
                showTodayDate();
            }
        }, portfolio.dateToday(), NO_MAX_DATE);
    }

    public void showContributionDatePicker(final int contributionIndex) {
        LocalDate maxDate = portfolio.dateToday().minusDays(1);
        view.displayDatePicker(new DatePickerAction() {
            @Override
            public void onDateSet(LocalDate date) {
                setContributionDate(contributionIndex, date);
                view.displayContributions(portfolio.contributions());
            }
        }, maxDate, maxDate);
    }

    public void setContributionAmount(int index, String amount) {
        portfolio.setContributionAmount(index, parseDouble(amount));
    }

    public void setValueToday(String value) {
        portfolio.setValueToday(parseDouble(value));
    }

    private void setContributionDate(int index, LocalDate date) {
        portfolio.setContributionDate(index, date);
    }

    private void showTodayDate() {
        view.displayTodayDate(portfolio.dateToday());
    }

    private Double parseDouble(String amount) {
        if (amount.isEmpty()) {
            return null;
        } else {
            return Double.parseDouble(amount);
        }
    }
}

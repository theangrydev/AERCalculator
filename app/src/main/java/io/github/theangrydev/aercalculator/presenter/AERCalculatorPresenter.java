package io.github.theangrydev.aercalculator.presenter;

import io.github.theangrydev.aercalculator.model.AERCalculator;
import io.github.theangrydev.aercalculator.model.Portfolio;
import io.github.theangrydev.aercalculator.model.UnknownAERException;
import io.github.theangrydev.aercalculator.view.SetContributionDate;
import io.github.theangrydev.aercalculator.view.SetDateToday;
import org.joda.time.LocalDate;

public class AERCalculatorPresenter {
    private static final LocalDate NO_MAX_DATE = new LocalDate(9999, 1, 1);

    private final AERCalculator aerCalculator = new AERCalculator();
    private final AERCalculatorView view;

    private final Portfolio portfolio;

    public AERCalculatorPresenter(AERCalculatorView view, Portfolio portfolio) {
        this.view = view;
        this.portfolio = portfolio;
    }

    public void initialise() {
        setDateToday(LocalDate.now());
        view.displayContributions(portfolio.contributions());
    }

    public void computeAER() {
        if (portfolio.hasUnknownValueToday() || portfolio.allContributionsAreIncomplete()) {
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
        view.selectContribution(portfolio.contributions().size() - 1);
    }

    public void showTodayDatePicker() {
        view.displayDatePicker(new SetDateToday(), portfolio.dateToday(), NO_MAX_DATE);
    }

    public void setDateToday(LocalDate date) {
        portfolio.setDateToday(date);
        view.displayTodayDate(portfolio.dateToday());
    }

    public void showContributionDatePicker(final int contributionIndex) {
        LocalDate maxDate = portfolio.dateToday().minusDays(1);
        view.displayDatePicker(new SetContributionDate(contributionIndex), maxDate, maxDate);
    }

    public void setContributionAmount(int index, String amount) {
        portfolio.setContributionAmount(index, parseDouble(amount));
        //displayContributions(); // TODO: #5 this doesn't work
    }

    public void setValueToday(String value) {
        portfolio.setValueToday(parseDouble(value));
    }

    public void setContributionDate(int index, LocalDate date) {
        portfolio.setContributionDate(index, date);
        view.displayContributions(portfolio.contributions());
    }

    private Double parseDouble(String amount) {
        if (amount.isEmpty()) {
            return null;
        } else {
            return Double.parseDouble(amount);
        }
    }
}

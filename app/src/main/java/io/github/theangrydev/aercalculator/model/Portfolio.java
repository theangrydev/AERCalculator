package io.github.theangrydev.aercalculator.model;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.theangrydev.aercalculator.model.Contribution.emptyContribution;
import static java.util.Collections.unmodifiableList;

public class Portfolio implements Serializable {

    private LocalDate dateToday;
    private Double valueToday;
    private List<Contribution> contributions;

    private Portfolio(LocalDate dateToday, Double valueToday, List<Contribution> contributions) {
        this.dateToday = dateToday;
        this.valueToday = valueToday;
        this.contributions = contributions;
    }

    public static Portfolio emptyPortfolio() {
        ArrayList<Contribution> contributions = new ArrayList<>();
        contributions.add(emptyContribution());
        return new Portfolio(null, null, contributions);
    }

    public boolean hasUnknownValueToday() {
        return valueToday == null;
    }

    public List<Contribution> contributions() {
        return unmodifiableList(contributions);
    }

    public boolean allContributionsAreIncomplete() {
        return filledInContributions().isEmpty();
    }

    public LocalDate dateToday() {
        return dateToday;
    }

    public void setDateToday(LocalDate dateToday) {
        this.dateToday = dateToday;
    }

    public double valueToday() {
        return valueToday;
    }

    public void setValueToday(Double valueToday) {
        this.valueToday = valueToday;
    }

    public void removeIncompleteContributions() {
        contributions = filledInContributions();
    }

    public void addEmptyContribution() {
        contributions.add(emptyContribution());
    }

    public void setContributionAmount(int index, Double amount) {
        Contribution contribution = contributions.get(index);
        contributions.set(index, contribution.withAmount(amount));
    }

    public void setContributionDate(int index, LocalDate date) {
        Contribution contribution = contributions.get(index);
        contributions.set(index, contribution.withDate(date));
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

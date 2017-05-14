package io.github.theangrydev.aercalculator;

import org.joda.time.LocalDate;

public class Contribution {

    public final LocalDate date;
    public final Double amount;

    private Contribution(LocalDate date, Double amount) {
        this.date = date;
        this.amount = amount;
    }

    public static Contribution contribution(LocalDate date, double amount) {
        return new Contribution(date, amount);
    }

    public static Contribution emptyContribution() {
        return new Contribution(null, null);
    }

    public Contribution withDate(LocalDate date) {
        return new Contribution(date, amount);
    }

    public Contribution withAmount(Double amount) {
        return new Contribution(date, amount);
    }

    public boolean hasDate() {
        return date != null;
    }

    public boolean hasAmount() {
        return amount != null;
    }
}


package io.github.theangrydev.aercalculator;

import org.joda.time.LocalDate;

public class Contribution {

    public final LocalDate date;
    public final double amount;

    Contribution(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public static Contribution contribution(LocalDate date, double amount) {
        return new Contribution(date, amount);
    }

    public static EmptyContribution emptyContribution() {
        return new EmptyContribution();
    }

    public Contribution withDate(LocalDate date) {
        return contribution(date, amount);
    }

    public Contribution withAmount(double amount) {
        return contribution(date, amount);
    }

    public boolean isEmpty() {
        return false;
    }
}

class EmptyContribution extends Contribution {

    EmptyContribution() {
        super(LocalDate.now(), 0);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}

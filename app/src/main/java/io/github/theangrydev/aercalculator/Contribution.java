package io.github.theangrydev.aercalculator;

import org.joda.time.LocalDate;

public class Contribution {
    public final LocalDate date;
    public final double amount;

    private Contribution(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public static Contribution contribution(LocalDate date, double amount) {
        return new Contribution(date, amount);
    }
}

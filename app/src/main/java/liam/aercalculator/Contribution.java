package liam.aercalculator;

import org.joda.time.DateTime;

public class Contribution {
    public final DateTime date;
    public final double amount;

    private Contribution(DateTime date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public static Contribution contribution(DateTime date, double amount) {
        return new Contribution(date, amount);
    }
}

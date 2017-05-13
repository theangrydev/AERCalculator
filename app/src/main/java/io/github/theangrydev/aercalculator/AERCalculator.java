package io.github.theangrydev.aercalculator;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;

public class AERCalculator {

    private static final int MAX_ITERATIONS = 50;
    private static final double MAX_EPSILON = 0.0000000001;
    private static final double INITIAL_GUESS = 0.1;
    private static final int DAYS_IN_YEAR = 365;

    public double computeAER(DateTime dateToday, double valueToday, List<Contribution> contributions) throws UnknownAERException {
        double rate = INITIAL_GUESS;
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            double resultAtRate = resultAtRate(contributions, rate, dateToday, valueToday);
            double resultAtRateDerivative = resultAtRateDerivative(contributions, rate, dateToday);

            double newRate = Math.max(-1.0, rate - resultAtRate / resultAtRateDerivative);
            double epsilonRate = Math.abs(newRate - rate);
            rate = newRate;

            if (epsilonRate <= MAX_EPSILON) {
                return rate * 100;
            }
        }
        throw new UnknownAERException();
    }

    private double resultAtRate(List<Contribution> contributions, double rate, DateTime dateToday, double valueToday) {
        double result = 0;
        for (Contribution contribution : contributions) {
            double yearsInvested = yearsBetween(contribution.date, dateToday);
            result += contribution.amount * Math.pow(rate + 1, yearsInvested);
        }
        return result - valueToday;
    }

    private double resultAtRateDerivative(List<Contribution> contributions, double rate, DateTime dateToday) {
        double derivative = 0;
        for (Contribution contribution : contributions) {
            double yearsInvested = yearsBetween(contribution.date, dateToday);
            derivative += contribution.amount * yearsInvested * Math.pow(rate + 1, yearsInvested - 1);
        }
        return derivative;
    }

    private double yearsBetween(DateTime date, DateTime dateToday) {
        return (double) Days.daysBetween(date, dateToday).getDays() / DAYS_IN_YEAR;
    }
}

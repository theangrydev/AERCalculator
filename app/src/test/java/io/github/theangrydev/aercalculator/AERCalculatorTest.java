package io.github.theangrydev.aercalculator;

import io.github.theangrydev.aercalculator.model.AERCalculator;
import io.github.theangrydev.aercalculator.model.Contribution;
import io.github.theangrydev.aercalculator.model.UnknownAERException;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.github.theangrydev.aercalculator.model.Contribution.contribution;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class AERCalculatorTest {

    private static final double TOLERANCE = 0.01;

    private final AERCalculator aerCalculator = new AERCalculator();

    @Test(expected = UnknownAERException.class)
    public void allContributionsTodayThrowsUnknownAERException() throws Exception {
        LocalDate dateToday = LocalDate.now();
        List<Contribution> contributions = asList(contribution(dateToday, anyValue()), contribution(dateToday, anyValue()));

        aerCalculator.computeAER(dateToday, anyValue(), contributions);
    }

    @Test
    public void noChange() throws Exception {
        LocalDate dateToday = LocalDate.now();
        double value = 100;

        LocalDate dateYesterday = dateToday.plusDays(-1);

        double aer = aerCalculator.computeAER(dateToday, value, singletonList(contribution(dateYesterday, value)));

        assertEquals(0, aer, TOLERANCE);
    }

    @Test
    public void loseItAllWithinAYear() throws Exception {
        LocalDate dateToday = LocalDate.now();
        double valueToday = 50;

        LocalDate dateYesterday = dateToday.plusDays(-1);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateYesterday, valueOneYearAgo)));

        assertEquals(-100, aer, TOLERANCE);
    }

    @Test
    public void loseHalfEachYear() throws Exception {
        LocalDate dateToday = LocalDate.now();
        double valueToday = 50;

        LocalDate dateOneYearAgo = dateToday.plusYears(-1);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateOneYearAgo, valueOneYearAgo)));

        assertEquals(-50, aer, TOLERANCE);
    }

    @Test
    public void oneContributionAYearAgoGain() throws Exception {
        LocalDate dateToday = LocalDate.now();
        double valueToday = 105;

        LocalDate dateOneYearAgo = dateToday.plusYears(-1);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateOneYearAgo, valueOneYearAgo)));

        assertEquals(5, aer, TOLERANCE);
    }

    @Test
    public void oneContributionTwoYearsAgo() throws Exception {
        LocalDate dateToday = LocalDate.now();
        double valueToday = 102.01;

        LocalDate dateOneYearAgo = dateToday.plusYears(-2);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateOneYearAgo, valueOneYearAgo)));

        assertEquals(1, aer, TOLERANCE);
    }

    private double anyValue() {
        return new Random().nextDouble();
    }
}
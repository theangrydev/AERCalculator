package liam.aercalculator;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static liam.aercalculator.Contribution.contribution;
import static org.junit.Assert.assertEquals;

public class AERCalculatorTest {

    private static final double TOLERANCE = 0.01;

    private final AERCalculator aerCalculator = new AERCalculator();

    @Test(expected = UnknownAERException.class)
    public void allContributionsTodayThrowsUnknownAERException() throws Exception {
        DateTime dateToday = DateTime.now();
        List<Contribution> contributions = asList(contribution(dateToday, anyValue()), contribution(dateToday, anyValue()));

        aerCalculator.computeAER(dateToday, anyValue(), contributions);
    }

    @Test
    public void oneContributionAYearAgo() throws Exception {
        DateTime dateToday = DateTime.now();
        double valueToday = 105;

        DateTime dateOneYearAgo = dateToday.plusYears(-1);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateOneYearAgo, valueOneYearAgo)));

        assertEquals(5, aer, TOLERANCE);
    }

    @Test
    public void oneContributionTwoYearsAgo() throws Exception {
        DateTime dateToday = DateTime.now();
        double valueToday = 102.01;

        DateTime dateOneYearAgo = dateToday.plusYears(-2);
        double valueOneYearAgo = 100;

        double aer = aerCalculator.computeAER(dateToday, valueToday, singletonList(contribution(dateOneYearAgo, valueOneYearAgo)));

        assertEquals(1, aer, TOLERANCE);
    }

    private double anyValue() {
        return 1;
    }
}
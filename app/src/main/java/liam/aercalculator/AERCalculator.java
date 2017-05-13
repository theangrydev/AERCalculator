package liam.aercalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static java.math.RoundingMode.CEILING;

public class AERCalculator {

    public BigDecimal computeAER(List<Contribution> contributions) {
        BigDecimal total = BigDecimal.ZERO;

        for (Contribution contribution : contributions) {
            total = total.add(contribution.amount);
        }
        return total.round(new MathContext(2, CEILING));
    }
}

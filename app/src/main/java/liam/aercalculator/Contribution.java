package liam.aercalculator;

import java.math.BigDecimal;
import java.util.Date;

public class Contribution {
    public final Date date;
    public final BigDecimal amount;

    public Contribution(Date date, BigDecimal amount) {
        this.date = date;
        this.amount = amount;
    }
}

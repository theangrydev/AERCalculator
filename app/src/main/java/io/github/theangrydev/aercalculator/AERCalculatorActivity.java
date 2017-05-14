package io.github.theangrydev.aercalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


public class AERCalculatorActivity extends AppCompatActivity {

    private static final LocalDate NO_MAX_DATE = new LocalDate(9999, 1, 1);

    private final AERCalculator aerCalculator = new AERCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        setTodayButton();
    }

    public void addContribution(View button) {
        getLayoutInflater().inflate(R.layout.contribution, contributionsTable());
    }

    public void showContributionDatePicker(View button) {
        LocalDate maxDate = todayButtonDate().minusDays(1);
        showDatePicker((Button) button, maxDate, maxDate);
    }

    public void showTodayDatePicker(View button) {
        showDatePicker((Button) button, todayButtonDate(), NO_MAX_DATE);
    }

    public void computeAER(View button) {
        List<Contribution> contributions = extractContributions();
        double valueToday = extractValueToday();
        String aer = computeAER(contributions, valueToday);
        displayAER(aer);
    }

    private void setTodayButton() {
        Button todayButton = (Button) findViewById(R.id.today_button);
        todayButton.setText(dateFormat().print(DateTime.now()));
    }

    private void showDatePicker(Button button, LocalDate initialDate, LocalDate maxDate) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setButton(button);
        datePicker.setInitialDate(initialDate);
        datePicker.setMaxDate(maxDate);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void displayAER(String aer) {
        TextView result = (TextView) findViewById(R.id.aer_result);
        result.setText(aer);
    }

    private double extractValueToday() {
        EditText valueToday = (EditText) findViewById(R.id.input_value_today);
        return parseDouble(valueToday);
    }

    private List<Contribution> extractContributions() {
        TableLayout contributionsTable = contributionsTable();
        int contributionCount = contributionsTable.getChildCount();

        List<Contribution> contributions = new ArrayList<>();
        for (int contributionIndex = 0; contributionIndex < contributionCount; contributionIndex++) {
            TableRow contribution = (TableRow) contributionsTable.getChildAt(contributionIndex);
            contributions.add(Contribution.contribution(date(contribution), amount(contribution)));
        }
        return contributions;
    }

    private String computeAER(List<Contribution> contributions, double valueToday) {
        try {
            double aer = aerCalculator.computeAER(todayButtonDate(), valueToday, contributions);
            return getString(R.string.aer, aer);
        } catch (UnknownAERException unknownAERException) {
            return getString(R.string.unknown_aer);
        }
    }

    private LocalDate todayButtonDate() {
        Button inputDate = (Button) findViewById(R.id.today_button);
        return date(inputDate);
    }

    private LocalDate date(TableRow contributionRow) {
        Button inputDate = (Button) contributionRow.findViewById(R.id.input_date);
        return date(inputDate);
    }

    private LocalDate date(Button dateButton) {
        String text = dateButton.getText().toString();
        if (text.equals(getString(R.string.enter))) {
            return LocalDate.now();
        }
        return dateFormat().parseLocalDate(text);
    }

    private DateTimeFormatter dateFormat() {
        return DateTimeFormat.forPattern(getString(R.string.date_format));
    }

    private double amount(TableRow contributionRow) {
        EditText inputAmount = (EditText) contributionRow.findViewById(R.id.input_amount);
        return parseDouble(inputAmount);
    }

    private double parseDouble(EditText numberInput) {
        try {
            return Double.parseDouble(numberInput.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private TableLayout contributionsTable() {
        return (TableLayout) findViewById(R.id.contributions_table);
    }

}

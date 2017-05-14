package io.github.theangrydev.aercalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import static io.github.theangrydev.aercalculator.Contribution.contribution;


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
        TableLayout contributionsTable = (TableLayout) findViewById(R.id.contributions_table);
        TableRow contribution = (TableRow) getLayoutInflater().inflate(R.layout.contribution, contributionsTable, false);
        contribution.setTag(contributionsTable.getChildCount());
        contributionsTable.addView(contribution);
    }

    public void showContributionDatePicker(View button) {
        LocalDate maxDate = todayButtonDate().minusDays(1);
        TableRow contribution = (TableRow) button.getParent();
        showDatePicker(new SetContributionDateAction(contribution.getTag()), maxDate, maxDate);
    }

    public void showTodayDatePicker(View button) {
        showDatePicker(new SetTodayDateAction(button.getId()), todayButtonDate(), NO_MAX_DATE);
    }

    public void computeAER(View button) {
        List<Contribution> contributions = extractContributions();
        double valueToday = extractValueToday();
        String aer = computeAER(contributions, valueToday);
        displayAER(aer);
    }

    private static class SetTodayDateAction implements DatePickerAction {
        private final int todayButtonId;

        public SetTodayDateAction(int todayButtonId) {
            this.todayButtonId = todayButtonId;
        }

        @Override
        public void onDateSet(FragmentActivity activity, int year, int month, int dayOfMonth) {
            Button dateButton = (Button) activity.findViewById(todayButtonId);
            dateButton.setText(formatDate(activity.getApplicationContext(), year, month, dayOfMonth));
        }
    }

    private static class SetContributionDateAction implements DatePickerAction {
        private final Object contributionTag;

        private SetContributionDateAction(Object contributionTag) {
            this.contributionTag = contributionTag;
        }

        @Override
        public void onDateSet(FragmentActivity activity, int year, int month, int dayOfMonth) {
            TableLayout contributionsTable = (TableLayout) activity.findViewById(R.id.contributions_table);
            TableRow contributionRow = (TableRow) contributionsTable.findViewWithTag(contributionTag);
            Button dateButton = (Button) contributionRow.findViewById(R.id.input_date);
            dateButton.setText(formatDate(activity.getApplicationContext(), year, month, dayOfMonth));
        }
    }

    private static String formatDate(Context context, int year, int month, int dayOfMonth) {
        return context.getString(R.string.date_format_parts, dayOfMonth, month + 1, year);
    }

    private void setTodayButton() {
        Button todayButton = (Button) findViewById(R.id.today_button);
        todayButton.setText(dateFormat().print(DateTime.now()));
    }

    private void showDatePicker(DatePickerAction action, LocalDate initialDate, LocalDate maxDate) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setArguments(action, initialDate, maxDate);
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
        TableLayout contributionsTable = (TableLayout) findViewById(R.id.contributions_table);
        int contributionCount = contributionsTable.getChildCount();

        List<Contribution> contributions = new ArrayList<>();
        for (int contributionIndex = 0; contributionIndex < contributionCount; contributionIndex++) {
            TableRow contributionRow = (TableRow) contributionsTable.getChildAt(contributionIndex);
            contributions.add(contribution(date(contributionRow), amount(contributionRow)));
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
}

package io.github.theangrydev.aercalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import io.github.theangrydev.aercalculator.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class AERCalculatorActivity extends AppCompatActivity {

    private final AERCalculator aerCalculator = new AERCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        setTodayButton();
    }

    public void addContribution(View button) {
        TableLayout tableLayout = contributionsTable();
        tableLayout.addView(makeContribution(button.getContext()));
    }

    public void showDatePicker(View button) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setButton((Button) button);
        datePicker.setToday(todayButtonDate());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public void computeAER(View button) throws ParseException {
        List<Contribution> contributions = extractContributions();
        double valueToday = extractValueToday();
        String aer = computeAER(contributions, valueToday);
        displayAER(aer);
    }

    private void setTodayButton() {
        Button todayButton = (Button) findViewById(R.id.today_button);
        todayButton.setText(dateFormat().print(DateTime.now()));
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
            return format(getString(R.string.aer), aer);
        } catch (UnknownAERException unknownAERException) {
            return getString(R.string.unknown_aer);
        }
    }

    private DateTime todayButtonDate() {
        Button inputDate = (Button) findViewById(R.id.today_button);
        return date(inputDate);
    }

    private DateTime date(TableRow contributionRow) {
        Button inputDate = (Button) contributionRow.findViewById(R.id.input_date);
        return date(inputDate);
    }

    private DateTime date(Button dateButton) {
        String text = dateButton.getText().toString();
        if (text.equals(getString(R.string.enter))) {
            return DateTime.now();
        }
        return dateFormat().parseDateTime(text).withTimeAtStartOfDay();
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

    private TableRow makeContribution(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return (TableRow) layoutInflater.inflate(R.layout.contribution, null);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private Button button;
        private DateTime today;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, today.getYear(), today.getMonthOfYear(), today.getDayOfMonth());
            datePickerDialog.getDatePicker().setMaxDate(today.plusDays(-1).getMillis());
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            button.setText(format(getString(R.string.date_format_parts), dayOfMonth, month + 1, year));
        }

        public void setToday(DateTime today) {
            this.today = today;
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }
}

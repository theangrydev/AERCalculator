package liam.aercalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static liam.aercalculator.Contribution.contribution;

public class AERCalculatorActivity extends AppCompatActivity {

    private final AERCalculator aerCalculator = new AERCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        addContribution(getBaseContext());
    }

    public void addContribution(View button) {
        addContribution(button.getContext());
    }

    public void showDatePicker(View button) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setButton((Button) button);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public void computeAER(View button) throws ParseException {
        TextView result = (TextView) findViewById(R.id.aer_result);
        result.setText(computeAER(extractContributions(), extractValueToday()));
    }

    private double extractValueToday() {
//        TextView valueToday = findViewById(R.d)
        return 0;
    }

    private List<Contribution> extractContributions() throws ParseException {
        TableLayout contributionsTable = contributionsTable();
        int contributionCount = contributionsTable.getChildCount() - 1;

        List<Contribution> contributions = new ArrayList<>();
        for (int contributionIndex = 0; contributionIndex < contributionCount; contributionIndex++) {
            TableRow contribution = (TableRow) contributionsTable.getChildAt(contributionIndex);
            contributions.add(contribution(date(contribution), amount(contribution)));
        }
        return contributions;
    }

    private String computeAER(List<Contribution> contributions, double valueToday) {
        try {
            double aer = aerCalculator.computeAER(DateTime.now(), valueToday, contributions);
            return format(ENGLISH, "%.2f%%", aer);
        } catch (UnknownAERException unknownAERException) {
            return "Unknown";
        }
    }

    private DateTime date(TableRow contributionRow) throws ParseException {
        Button inputDate = (Button) contributionRow.findViewById(R.id.input_date);
        String text = inputDate.getText().toString();
        return DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(text).withTimeAtStartOfDay();
    }

    private double amount(TableRow contributionRow) {
        EditText inputAmount = (EditText) contributionRow.findViewById(R.id.input_amount);
        return Double.parseDouble(inputAmount.getText().toString());
    }

    private void addContribution(Context context) {
        TableLayout tableLayout = contributionsTable();
        tableLayout.addView(makeContribution(context), tableLayout.getChildCount() - 1);
        tableLayout.requestLayout();
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DateTime today = DateTime.now();
            return new DatePickerDialog(getActivity(), this, today.getYear(), today.getMonthOfYear(), today.getDayOfMonth());
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            button.setText(format(ENGLISH, "%02d/%02d/%d", dayOfMonth, month, year));
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }
}

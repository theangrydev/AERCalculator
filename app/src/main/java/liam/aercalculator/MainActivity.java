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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public class MainActivity extends AppCompatActivity {

    private final AERCalculator aerCalculator = new AERCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        TableLayout contributionsTable = contributionsTable();
        int contributionCount = contributionsTable.getChildCount() - 1;

        List<Contribution> contributions = new ArrayList<>();
        for (int contributionIndex = 0; contributionIndex < contributionCount; contributionIndex++) {
            TableRow contribution = (TableRow) contributionsTable.getChildAt(contributionIndex);
            contributions.add(new Contribution(date(contribution), amount(contribution)));
        }

        BigDecimal aer = aerCalculator.computeAER(contributions);
        TextView result = (TextView) findViewById(R.id.aer_result);
        result.setText(format(ENGLISH, "%s%%", aer));
    }

    private Date date(TableRow contributionRow) throws ParseException {
        Button inputDate = (Button) contributionRow.findViewById(R.id.input_date);
        String text = inputDate.getText().toString();
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(text);
        } catch (ParseException e) {
            return new Date();
        }
    }

    private BigDecimal amount(TableRow contributionRow) {
        EditText inputAmount = (EditText) contributionRow.findViewById(R.id.input_amount);
        return new BigDecimal(inputAmount.getText().toString());
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
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR);
            int month = today.get(Calendar.MONTH);
            int day = today.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
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

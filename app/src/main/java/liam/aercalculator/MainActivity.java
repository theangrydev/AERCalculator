package liam.aercalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Calendar;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDateButton();
        setUpAddMoreButton();
    }

    private void setUpAddMoreButton() {
        final Button button = (Button) findViewById(R.id.enter_more_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContribution();
            }
        });
    }

    private void setUpDateButton() {
        final Button button = (Button) findViewById(R.id.input_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(button);
            }
        });
    }

    private void addContribution() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.contributions_table);
//        TableRow tableRow = (TableRow) tableLayout.getChildAt(0);

        TableRow tableRow = new TableRow(tableLayout.getContext());
        tableLayout.addView(tableRow, tableLayout.getChildCount() - 1);
    }

    private void showDatePicker(Button button) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setButton(button);
        datePicker.show(getFragmentManager(), "datePicker");
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

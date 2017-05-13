package liam.aercalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
    }

    public void addContribution(View view) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.contributions_table);
        tableLayout.addView(makeContribution(view.getContext()), tableLayout.getChildCount() - 1);
    }

    public void showDatePicker(View button) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setButton((Button) button);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private TableRow makeContribution(Context context) {
        LayoutInflater layoutInflater = getLayoutInflater();
        TableRow contribution = new TableRow(context);
        layoutInflater.inflate(R.layout.contribution, contribution);
        return contribution;
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

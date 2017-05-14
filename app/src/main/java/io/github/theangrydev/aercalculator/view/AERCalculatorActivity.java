package io.github.theangrydev.aercalculator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import io.github.theangrydev.aercalculator.R;
import io.github.theangrydev.aercalculator.model.Contribution;
import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import io.github.theangrydev.aercalculator.presenter.AERCalculatorView;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

7

public class AERCalculatorActivity extends AppCompatActivity implements AERCalculatorView {

    private final AERCalculatorPresenter presenter = new AERCalculatorPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        presenter.initialise();
        listenToTodayValue();
    }

    public void addContribution(View addContributionButton) {
        presenter.addContribution();
    }

    public void showTodayDatePicker(View button) {
        presenter.showTodayDatePicker();
    }

    public void computeAER(View button) {
        presenter.computeAER();
    }

    @Override
    public void displayContributions(List<Contribution> contributions) {
        ListView contributionsTable = (ListView) findViewById(R.id.contributions_table);
        ContributionAdapter contributionAdapter = new ContributionAdapter(this, contributions, presenter);
        contributionsTable.setAdapter(contributionAdapter);
    }

    @Override
    public void displayAER(double aer) {
        TextView result = (TextView) findViewById(R.id.aer_result);
        result.setText(getString(R.string.aer, aer));
    }

    @Override
    public void displayUnknownAER() {
        TextView result = (TextView) findViewById(R.id.aer_result);
        result.setText(getText(R.string.unknown_aer));
    }

    @Override
    public void displayDatePicker(DatePickerAction action, LocalDate initialDate, LocalDate maxDate) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setArguments(action, initialDate, maxDate);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void displayTodayDate(LocalDate todayDate) {
        Button today = (Button) findViewById(R.id.today_button);
        today.setText(dateFormat().print(todayDate));
    }

    @Override
    public void scrollToBottomOfContributions() {
        ListView contributionsTable = (ListView) findViewById(R.id.contributions_table);
        contributionsTable.setSelection(contributionsTable.getCount() - 1);
    }

    private DateTimeFormatter dateFormat() {
        return DateTimeFormat.forPattern(getString(R.string.date_format));
    }

    private void listenToTodayValue() {
        final TextView valueToday = (TextView) findViewById(R.id.input_value_today);
        valueToday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.setValueToday(editable.toString());
            }
        });
    }
}

package io.github.theangrydev.aercalculator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AERCalculatorActivity extends AppCompatActivity implements AERCalculatorView {

    private final AERCalculatorPresenter presenter = new AERCalculatorPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        presenter.initialise();
        listenToTodayValueChanges();
        listenToTodayDateButtonClicks();
        listenToAddContributionButtonClicks();
        listenToComputeAERButtonClicks();
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

    private void listenToTodayDateButtonClicks() {
        Button button = (Button) findViewById(R.id.today_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                presenter.showTodayDatePicker();
            }
        });
    }

    private void listenToAddContributionButtonClicks() {
        Button button = (Button) findViewById(R.id.add_more_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                presenter.addContribution();
            }
        });
    }

    private void listenToComputeAERButtonClicks() {
        Button button = (Button) findViewById(R.id.compute_aer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                presenter.computeAER();
            }
        });
    }

    private void listenToTodayValueChanges() {
        final TextView valueToday = (TextView) findViewById(R.id.input_value_today);
        valueToday.addTextChangedListener(new TextChangedWatcher() {

            @Override
            protected void textChanged(String text) {
                presenter.setValueToday(text);
            }
        });
    }
}

package io.github.theangrydev.aercalculator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import io.github.theangrydev.aercalculator.R;
import io.github.theangrydev.aercalculator.model.Contribution;
import io.github.theangrydev.aercalculator.model.Portfolio;
import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import io.github.theangrydev.aercalculator.presenter.AERCalculatorView;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static io.github.theangrydev.aercalculator.model.Portfolio.emptyPortfolio;

public class AERCalculatorActivity extends AppCompatActivity implements AERCalculatorView {

    private static final String PORTFOLIO_KEY = "portfolio";

    private AERCalculatorPresenter presenter;
    private Portfolio portfolio;

    public AERCalculatorPresenter presenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aer_calculator);
        portfolio = loadPortfolio(savedInstanceState);
        presenter = new AERCalculatorPresenter(this, portfolio);
        presenter.initialise();
        listenToTodayValueChanges();
        listenToTodayDateButtonClicks();
        listenToAddContributionButtonClicks();
        listenToComputeAERButtonClicks();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PORTFOLIO_KEY, portfolio);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void displayContributions(List<Contribution> contributions) {
        contributionsTable().setAdapter(new ContributionAdapter(this, contributions, presenter));
    }

    @Override
    public void selectContribution(int selectedIndex) {
        contributionsTable().setSelection(selectedIndex);
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

    private Portfolio loadPortfolio(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return emptyPortfolio();
        }
        Portfolio portfolio = (Portfolio) savedInstanceState.getSerializable(PORTFOLIO_KEY);
        if (portfolio == null) {
            return emptyPortfolio();
        } else {
            return portfolio;
        }
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

    private ListView contributionsTable() {
        return (ListView) findViewById(R.id.contributions_table);
    }
}

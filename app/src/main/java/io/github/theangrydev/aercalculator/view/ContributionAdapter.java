package io.github.theangrydev.aercalculator.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import io.github.theangrydev.aercalculator.R;
import io.github.theangrydev.aercalculator.model.Contribution;
import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class ContributionAdapter extends ArrayAdapter<Contribution> {

    private final AERCalculatorPresenter presenter;

    public ContributionAdapter(Context context, List<Contribution> contributions, AERCalculatorPresenter presenter) {
        super(context, 0, contributions);
        this.presenter = presenter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Contribution contribution = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contribution, parent, false);
        }
        updateDate(position, convertView, contribution);
        updateAmount(position, convertView, contribution);
        return convertView;
    }

    private void updateAmount(final int position, View convertView, Contribution contribution) {
        TextView amount = (TextView) convertView.findViewById(R.id.input_amount);
        amount.setText(contributionAmount(contribution));
        clearOnFocus(amount);
        listenToAmountChanges(position, amount);
    }

    private void updateDate(final int position, View convertView, Contribution contribution) {
        Button date = (Button) convertView.findViewById(R.id.input_date);
        date.setText(contributionDate(contribution));
        listenToContributionDateButtonClicks(position, date);
    }

    private void clearOnFocus(final TextView textView) {
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView.setText("");
                }
            }
        });
    }

    private String contributionAmount(Contribution contribution) {
        if (contribution.hasAmount()) {
            return decimalFormat().format(contribution.amount);
        } else {
            return "";
        }
    }

    private String contributionDate(Contribution contribution) {
        if (contribution.hasDate()) {
            return dateFormat().print(contribution.date);
        } else {
            return getString(R.string.enter);
        }
    }

    private DecimalFormat decimalFormat() {
        return new DecimalFormat(getString(R.string.decimal_format));
    }

    private DateTimeFormatter dateFormat() {
        return DateTimeFormat.forPattern(getString(R.string.date_format));
    }

    private String getString(int id) {
        return getContext().getString(id);
    }

    private void listenToContributionDateButtonClicks(final int position, Button date) {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showContributionDatePicker(position);
            }
        });
    }

    private void listenToAmountChanges(final int position, TextView amount) {
        amount.addTextChangedListener(new TextChangedWatcher() {

            @Override
            protected void textChanged(String text) {
                presenter.setContributionAmount(position, text);
            }
        });
    }
}

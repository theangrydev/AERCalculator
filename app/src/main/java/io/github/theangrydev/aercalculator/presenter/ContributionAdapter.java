package io.github.theangrydev.aercalculator.presenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import io.github.theangrydev.aercalculator.Contribution;
import io.github.theangrydev.aercalculator.R;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
        final TextView amount = (TextView) convertView.findViewById(R.id.input_amount);
        amount.setText(contributionAmount(contribution));
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    amount.setText("");
                }
            }
        });
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.setContributionAmount(position, editable.toString());
            }
        });
    }

    private void updateDate(final int position, View convertView, Contribution contribution) {
        Button date = (Button) convertView.findViewById(R.id.input_date);
        date.setText(contributionDate(contribution));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showContributionDatePicker(position);
            }
        });
    }

    private String contributionAmount(Contribution contribution) {
        if (contribution.isEmpty()) {
            return "";
        } else {
            return String.valueOf(contribution.amount);
        }
    }

    private String contributionDate(Contribution contribution) {
        if (contribution.isEmpty()) {
            return getString(R.string.enter);
        } else {
            return dateFormat().print(contribution.date);
        }
    }

    private DateTimeFormatter dateFormat() {
        return DateTimeFormat.forPattern(getString(R.string.date_format));
    }

    private String getString(int id) {
        return getContext().getString(id);
    }
}

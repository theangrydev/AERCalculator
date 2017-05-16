package io.github.theangrydev.aercalculator.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
            convertView = createView(parent);
        }
        Button dateButton = dateButton(convertView);
        dateButton.setTag(position);
        dateButton.setText(contributionDate(contribution));

        EditText amountText = amountEditText(convertView);
        amountText.setTag(position);
        amountText.setText(contributionAmount(contribution));
        return convertView;
    }

    private View createView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contribution, parent, false);

        EditText amountText = amountEditText(view);
        amountText.setSelectAllOnFocus(true);
        listenToAmountChanges(amountText);

        Button dateButton = dateButton(view);
        listenToContributionDateButtonClicks(dateButton);
        return view;
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

    private void listenToContributionDateButtonClicks(final Button dateButton) {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showContributionDatePicker((Integer) dateButton.getTag());
            }
        });
    }

    private void listenToAmountChanges(final TextView amountText) {
        amountText.addTextChangedListener(new TextChangedWatcher() {

            @Override
            protected void textChanged(String text) {
                presenter.setContributionAmount((Integer) amountText.getTag(), text);
            }
        });
    }

    private Button dateButton(View convertView) {
        return (Button) convertView.findViewById(R.id.input_date);
    }

    private EditText amountEditText(View convertView) {
        return (EditText) convertView.findViewById(R.id.input_amount);
    }
}

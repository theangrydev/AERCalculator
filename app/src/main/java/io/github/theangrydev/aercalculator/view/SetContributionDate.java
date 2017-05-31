package io.github.theangrydev.aercalculator.view;

import io.github.theangrydev.aercalculator.presenter.AERCalculatorPresenter;
import org.joda.time.LocalDate;

public class SetContributionDate implements DatePickerAction {
    private final int contributionIndex;

    public SetContributionDate(int contributionIndex) {
        this.contributionIndex = contributionIndex;
    }

    @Override
    public void onDateSet(AERCalculatorPresenter presenter, LocalDate date) {
        presenter.setContributionDate(contributionIndex, date);
    }
}

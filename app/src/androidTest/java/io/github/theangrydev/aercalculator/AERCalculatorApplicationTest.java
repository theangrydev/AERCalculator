package io.github.theangrydev.aercalculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;
import io.github.theangrydev.aercalculator.view.AERCalculatorActivity;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static io.github.theangrydev.aercalculator.WithIndex.withIndex;
import static org.hamcrest.Matchers.*;
import static org.joda.time.DateTime.now;
import static org.joda.time.format.DateTimeFormat.forPattern;

@RunWith(AndroidJUnit4.class)
public class AERCalculatorApplicationTest {

    @Rule
    public ActivityTestRule<AERCalculatorActivity> activityRule = new ActivityTestRule<>(AERCalculatorActivity.class);

    private int currentContributionIndex;

    @Test
    public void computesAER() throws Exception {
        givenDateTodayIsSetTo(2017, 5, 14);
        andValueTodayIsSetTo(210);

        andContributionDateIsSetTo(2016, 5, 14);
        andContributionAmountIsSetTo(100);

        andAnotherContributionIsAdded();
        andContributionDateIsSetTo(2016, 8, 14);
        andContributionAmountIsSetTo(100);

        whenComputeAERIsClicked();

        thenTheComputedAERIs("5.74%");
    }

    @Test
    public void defaultsToToday() throws Exception {
        theDefaultDayIsToday();
    }

    private void givenDateTodayIsSetTo(int year, int monthOfYear, int dayOfMonth) {
        onView(withId(R.id.today_button)).perform(click());
        setDatePicker(year, monthOfYear, dayOfMonth);
    }

    private void andValueTodayIsSetTo(int value) {
        typeTextInto(withId(R.id.input_value_today), value);
    }

    private void andContributionDateIsSetTo(int year, int monthOfYear, int dayOfMonth) {
        onView(withIndex(withId(R.id.input_date), currentContributionIndex)).perform(click());
        setDatePicker(year, monthOfYear, dayOfMonth);
    }

    private void andContributionAmountIsSetTo(int amount) {
        typeTextInto(withIndex(withId(R.id.input_amount), currentContributionIndex), amount);
    }

    private void whenComputeAERIsClicked() {
        onView(withId(R.id.compute_aer)).perform(click());
    }

    private void thenTheComputedAERIs(String aer) {
        onView(withId(R.id.aer_result)).check(matches(withText("AER: " + aer)));
    }

    private void andAnotherContributionIsAdded() {
        currentContributionIndex++;
        onView(withId(R.id.add_more_button)).perform(click());
    }

    private void theDefaultDayIsToday() {
        onView(withId(R.id.today_button)).check(matches(withText(forPattern("dd/MM/yyyy").print(now()))));
    }

    private void typeTextInto(Matcher<View> viewMatcher, int amount) {
        onView(viewMatcher).perform(typeText(String.valueOf(amount)), closeSoftKeyboard());
    }

    private void setDatePicker(int year, int monthOfYear, int dayOfMonth) {
        onView(datePicker()).perform(setDate(year, monthOfYear, dayOfMonth));
        onView(datePickerDone()).perform(click());
    }

    private Matcher<View> datePicker() {
        return withClassName(equalTo(DatePicker.class.getName()));
    }

    // TODO: #3 different API levels have a different name for the button, is there a better way to click it?
    private Matcher<View> datePickerDone() {
        return withText(anyOf(is("OK"), is("Set"), is("Done")));
    }
}

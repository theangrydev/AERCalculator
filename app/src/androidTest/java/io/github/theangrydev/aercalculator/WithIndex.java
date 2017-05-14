package io.github.theangrydev.aercalculator;

import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class WithIndex extends TypeSafeMatcher<View> {
    private final int targetIndex;
    private final Matcher<View> matcher;

    private View matched;
    private int currentIndex;

    private WithIndex(int targetIndex, Matcher<View> matcher) {
        this.targetIndex = targetIndex;
        this.matcher = matcher;
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new WithIndex(index, matcher);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with target index: ");
        description.appendValue(targetIndex);
        matcher.describeTo(description);
    }

    @Override
    public boolean matchesSafely(View view) {
        if (matcher.matches(view) && currentIndex++ == targetIndex) {
            matched = view;
        }
        return matched == view;
    }
}

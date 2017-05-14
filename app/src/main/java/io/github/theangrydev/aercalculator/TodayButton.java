package io.github.theangrydev.aercalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class TodayButton extends Button {

    public TodayButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setText(dateFormat().print(DateTime.now()));
    }

    private DateTimeFormatter dateFormat() {
        return forPattern(getContext().getString(R.string.date_format));
    }
}

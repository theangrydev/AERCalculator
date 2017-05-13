package io.github.theangrydev.aercalculator;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

public class AERCalculatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}

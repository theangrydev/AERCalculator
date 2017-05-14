package io.github.theangrydev.aercalculator.view;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextChangedWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        textChanged(editable.toString());
    }

    protected abstract void textChanged(String text);
}

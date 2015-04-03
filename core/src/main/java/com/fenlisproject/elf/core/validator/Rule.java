package com.fenlisproject.elf.core.validator;

import android.widget.TextView;

public abstract class Rule {

    private String mValidationMessage;

    public Rule(String mValidationMessage) {
        this.mValidationMessage = mValidationMessage;
    }

    public abstract boolean validate(TextView view);

    public void apply(TextView view) {
        if (view.getError() == null) {
            view.setError(getValidationMessage());
        } else {
            String errorValue = view.getError().toString();
            view.setError(errorValue + "\n" + getValidationMessage());
        }
    }

    public String getValidationMessage() {
        return mValidationMessage;
    }
}

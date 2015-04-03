package com.fenlisproject.elf.core.validator.rule;

import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class MinimumLength extends Rule {

    private int minLength;

    public MinimumLength(String message, int minLength) {
        super(message);
        this.minLength = minLength;
    }

    @Override
    public boolean validate(TextView view) {
        return view.length() >= minLength;
    }
}

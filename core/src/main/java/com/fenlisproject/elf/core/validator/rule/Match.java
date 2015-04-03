package com.fenlisproject.elf.core.validator.rule;

import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class Match extends Rule {

    private TextView compared;

    public Match(String message, TextView compared) {
        super(message);
        this.compared = compared;
    }

    @Override
    public boolean validate(TextView view) {
        return view.getText().toString().equals(compared.getText().toString());
    }
}

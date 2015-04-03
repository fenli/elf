package com.fenlisproject.elf.core.validator.rule;

import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class Trimmed extends Rule {

    public Trimmed() {
        super(null);
    }

    @Override
    public boolean validate(TextView view) {
        view.setText(view.getText().toString().trim());
        return true;
    }
}

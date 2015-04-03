package com.fenlisproject.elf.core.validator.rule;

import android.util.Patterns;
import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class ValidEmail extends Rule {

    public ValidEmail(String message) {
        super(message);
    }

    @Override
    public boolean validate(TextView view) {
        return Patterns.EMAIL_ADDRESS.matcher(view.getText()).matches();
    }
}

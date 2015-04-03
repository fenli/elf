package com.fenlisproject.elf.core.validator.rule;

import android.util.Patterns;
import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class ValidPhoneNumber extends Rule {

    public ValidPhoneNumber(String message) {
        super(message);
    }

    @Override
    public boolean validate(TextView view) {
        return Patterns.PHONE.matcher(view.getText()).matches();
    }
}

package com.fenlisproject.elf.core.validator.rule;

import android.util.Patterns;
import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class ValidWebUrl extends Rule {

    public ValidWebUrl(String message) {
        super(message);
    }

    @Override
    public boolean validate(TextView view) {
        return Patterns.WEB_URL.matcher(view.getText()).matches();
    }
}

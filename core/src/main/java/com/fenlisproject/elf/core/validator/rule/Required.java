package com.fenlisproject.elf.core.validator.rule;

import android.widget.TextView;

import com.fenlisproject.elf.core.validator.Rule;

public class Required extends Rule {

    public Required(String message) {
        super(message);
    }

    @Override
    public boolean validate(TextView view) {
        return view.length() > 0;
    }
}

package com.fenlisproject.elf.core.validator;

import android.widget.EditText;

import com.fenlisproject.elf.core.validator.rule.Required;
import com.fenlisproject.elf.core.validator.rule.Trimmed;
import com.fenlisproject.elf.core.validator.rule.ValidEmail;
import com.fenlisproject.elf.core.validator.rule.ValidPhoneNumber;
import com.fenlisproject.elf.core.validator.rule.ValidWebUrl;
import com.fenlisproject.elf.core.widget.ExtendedEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormValidator {

    public static final Rule REQUIRED = new Required("This field is required");
    public static final Rule TRIMMED = new Trimmed();
    public static final Rule VALID_EMAIL = new ValidEmail("Please enter valid email address");
    public static final Rule VALID_WEB_URL = new ValidWebUrl("Please enter valid url");
    public static final Rule VALID_PHONE = new ValidPhoneNumber("Please enter valid phone number");
    private List<Validation> validations;

    public FormValidator() {
        validations = new ArrayList<>();
    }

    public FormValidator addValidation(EditText view, Rule... rules) {
        List<Rule> allRules = new ArrayList<>();
        if (view instanceof ExtendedEditText) {
            allRules.addAll(((ExtendedEditText) view).getRules());
        }
        allRules.addAll(Arrays.asList(rules));
        validations.add(new Validation(view, allRules));
        return this;
    }

    public boolean validate() {
        boolean result = true;
        for (Validation validation : validations) {
            validation.reset();
            for (Rule rule : validation.getRules()) {
                if (!rule.validate(validation.getView())) {
                    result = result && false;
                    rule.apply(validation.getView());
                }
            }
        }
        return result;
    }

    static class Validation {

        private EditText view;
        private List<Rule> rules;

        public Validation(EditText view, List<Rule> rules) {
            this.view = view;
            this.rules = rules;
        }

        public EditText getView() {
            return view;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void reset() {
            view.setError(null);
        }
    }
}

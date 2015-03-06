package com.fenlisproject.elf.core.util;

import android.widget.EditText;

public final class FormHelper {

    public static boolean validateRequired(EditText field) {
        boolean isValid;
        if (field.getText().toString().trim().length() == 0) {
            field.setError(field.getHint() + " is required");
            isValid = false;
        } else {
            field.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public static boolean validateEmail(EditText field) {
        boolean isValid;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(field.getText().toString().trim()).matches()) {
            field.setError("Your email is not valid");
            isValid = false;
        } else {
            field.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public static String getValue(EditText field) {
        return field.getText().toString().trim();
    }
}
package com.fenlisproject.elf.sample;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.annotation.OnClick;
import com.fenlisproject.elf.core.annotation.ViewId;
import com.fenlisproject.elf.core.base.BaseActivity;
import com.fenlisproject.elf.core.validator.FormValidator;
import com.fenlisproject.elf.core.widget.ExtendedEditText;

@ContentView(R.layout.activity_form_validation)
public class FormValidationActivity extends BaseActivity {

    @ViewId(R.id.name)
    ExtendedEditText name;

    @ViewId(R.id.password)
    ExtendedEditText password;

    @ViewId(R.id.confirm_password)
    ExtendedEditText confirmPassword;

    private FormValidator validator;

    @Override
    protected void onContentViewCreated() {
        validator = new FormValidator()
                .addValidation(name)
                .addValidation(password)
                .addValidation(confirmPassword);
    }

    @OnClick(R.id.submit_button)
    public void submit() {
        if (validator.validate()) {
            // Send form data to server
        }
    }
}

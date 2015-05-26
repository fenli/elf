package com.fenlisproject.elf.sample;

import android.content.Context;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.base.BaseDialog;

import com.fenlisproject.elf.sample.R;

@ContentView(R.layout.activity_main)
public class CustomDialog extends BaseDialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onContentViewCreated() {

    }
}

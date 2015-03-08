package com.fenlisproject.elf.core.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.fenlisproject.elf.core.annotation.Binder;
import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.handler.BaseTaskExecutor;

public abstract class BaseDialog extends Dialog implements BaseEventListener {

    public BaseDialog(Context context) {
        super(context);
        initLayout();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        initLayout();
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initLayout();
    }

    private void initLayout() {
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            setContentView(contentView.value());
        }
        Binder.bindView(this, null);
        Binder.bindEventListener(this, null);
        onContentViewCreated();
    }

    protected abstract void onContentViewCreated();

    @Override
    public void onClick(View v) {
        BaseTaskExecutor.executeOnClickListener(this, v.getId());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseTaskExecutor.executeOnItemClickListener(this, parent.getId(), position);
    }
}

/*
* Copyright (C) 2015 Steven Lewi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.fenlisproject.elf.core.base;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.framework.ElfBinder;
import com.fenlisproject.elf.core.framework.ElfCaller;

public abstract class BaseDialog extends AppCompatDialog implements BaseEventListener {

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

    protected void initLayout() {
        onBeforeSetContentView();
        ContentView contentView = getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            setContentView(contentView.value());
        }
        ElfBinder.bindView(this, null);
        onBeforeBindEventListener();
        ElfBinder.bindEventListener(this, null);
        ElfBinder.bindAnimation(this);
        onContentViewCreated();
    }

    protected void onBeforeSetContentView() {
    }

    protected void onBeforeBindEventListener() {
    }

    protected abstract void onContentViewCreated();

    @Override
    public void onClick(View v) {
        ElfCaller.callOnClickListener(this, v.getId());
    }

    @Override
    public boolean onLongClick(View v) {
        return ElfCaller.callOnLongClickListener(this, v.getId());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ElfCaller.callOnFocusChangeListener(this, v.getId(), hasFocus);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return ElfCaller.callOnTouchListener(this, v.getId(), event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ElfCaller.callOnItemClickListener(this, parent.getId(), position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return ElfCaller.callOnItemLongClickListener(this, parent.getId(), position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ElfCaller.callOnItemSelectedListener(this, parent.getId(), position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ElfCaller.callOnCheckedChangedListener(this, buttonView.getId(), isChecked);
    }
}

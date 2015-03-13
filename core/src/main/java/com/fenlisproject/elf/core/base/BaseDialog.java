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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.fenlisproject.elf.core.framework.ElfBinder;
import com.fenlisproject.elf.core.annotation.ContentView;
import com.fenlisproject.elf.core.framework.ElfCaller;

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
        ElfBinder.bindView(this, null);
        ElfBinder.bindEventListener(this, null);
        onContentViewCreated();
    }

    protected abstract void onContentViewCreated();

    @Override
    public void onClick(View v) {
        ElfCaller.callOnClickListener(this, v.getId());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ElfCaller.callOnItemClickListener(this, parent.getId(), position);
    }
}

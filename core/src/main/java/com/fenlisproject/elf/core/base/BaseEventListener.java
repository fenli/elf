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

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

interface BaseEventListener extends
        View.OnClickListener, View.OnLongClickListener, View.OnFocusChangeListener,
        View.OnTouchListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {

    @Override
    void onClick(View v);

    @Override
    boolean onLongClick(View v);

    @Override
    void onFocusChange(View v, boolean hasFocus);

    @Override
    boolean onTouch(View v, MotionEvent event);

    @Override
    void onItemClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    void onItemSelected(AdapterView<?> parent, View view, int position, long id);

    @Override
    void onNothingSelected(AdapterView<?> parent);

    @Override
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
}

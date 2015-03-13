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

import android.view.View;
import android.widget.AdapterView;

interface BaseEventListener extends View.OnClickListener, AdapterView.OnItemClickListener {

    @Override
    public void onClick(View v);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id);
}

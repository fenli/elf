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

package com.fenlisproject.elf.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fenlisproject.elf.R;
import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.MemoryStorage;

public class ExtendedTextView extends TextView {

    public ExtendedTextView(Context context) {
        super(context);
    }

    public ExtendedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFontFace(context, attrs);
    }

    public ExtendedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFontFace(context, attrs);
    }

    public void initFontFace(Context context, AttributeSet attrs) {
        MemoryStorage<Typeface> fontCache = ((BaseApplication) context.getApplicationContext())
                .getFontCache();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExtendedView, 0, 0);
        try {
            String fontName = ta.getString(R.styleable.ExtendedView_fontName);
            if (fontName != null) {
                Typeface tf = fontCache.get(fontName);
                if (tf == null) {
                    tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
                    fontCache.put(fontName, tf);
                }
                setTypeface(tf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }
}
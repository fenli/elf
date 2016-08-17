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
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.fenlisproject.elf.R;
import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.MemoryStorage;

public class ExtendedButton extends AppCompatButton implements FontFace, ExtendedAttributes {

    public ExtendedButton(Context context) {
        this(context, null);
    }

    public ExtendedButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public ExtendedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initExtendedAttributes(attrs, defStyleAttr);
        }
    }

    @Override
    public void initExtendedAttributes(AttributeSet attrs, int defStyleAttr) {
        if (getContext().getApplicationContext() instanceof BaseApplication) {
            if (attrs != null) {
                TypedArray style = getContext().obtainStyledAttributes(
                        attrs, R.styleable.ExtendedTextView, defStyleAttr, 0);
                String fontName = style.getString(R.styleable.ExtendedTextView_fontName);
                String fontFormat = style.getString(R.styleable.ExtendedTextView_fontFormat);
                style.recycle();
                setFontFace(fontName, fontFormat != null ? fontFormat : FORMAT_TTF);
            }
        } else {
            throw new RuntimeException("Your application class must extends BaseApplication");
        }
    }

    @Override
    public void setFontFace(String fontName) {
        setFontFace(fontName, FORMAT_TTF);
    }

    @Override
    public void setFontFace(String fontName, String fontFormat) {
        BaseApplication app = ((BaseApplication) getContext().getApplicationContext());
        MemoryStorage<Typeface> fontCache = app.getFontCache();
        if (fontName != null) {
            try {
                Typeface tf = fontCache.get(fontName);
                if (tf == null) {
                    String fontFilePath = String.format("fonts/%s.%s", fontName, fontFormat);
                    tf = Typeface.createFromAsset(getContext().getAssets(), fontFilePath);
                    fontCache.put(fontName, tf);
                }
                setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

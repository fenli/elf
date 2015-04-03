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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fenlisproject.elf.R;
import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.MemoryStorage;
import com.fenlisproject.elf.core.validator.Rule;
import com.fenlisproject.elf.core.validator.rule.Match;
import com.fenlisproject.elf.core.validator.rule.MinimumLength;
import com.fenlisproject.elf.core.validator.rule.Required;
import com.fenlisproject.elf.core.validator.rule.Trimmed;
import com.fenlisproject.elf.core.validator.rule.ValidEmail;

import java.util.ArrayList;
import java.util.List;

public class ExtendedTextView extends TextView implements FontFace, ExtendedAttributes {

    private String mFontName;
    private String mFontFormat;
    private boolean isRequired;
    private boolean isTrimmed;
    private boolean mustValidEmail;
    private int mMinLength;
    private int mMatchValueOf;

    public ExtendedTextView(Context context) {
        super(context);
        initExtendedAttributes(null);
    }

    public ExtendedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initExtendedAttributes(attrs);
    }

    public ExtendedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initExtendedAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initExtendedAttributes(attrs);
    }

    public void initExtendedAttributes(AttributeSet attrs) {
        if (getContext().getApplicationContext() instanceof BaseApplication) {
            if (attrs != null) {
                TypedArray ta = getContext().obtainStyledAttributes(
                        attrs, R.styleable.ExtendedTextView, 0, 0);
                String fontName = ta.getString(R.styleable.ExtendedTextView_fontName);
                String fontFormat = ta.getString(R.styleable.ExtendedTextView_fontFormat);
                isRequired = ta.getBoolean(R.styleable.ExtendedTextView_required, false);
                isTrimmed = ta.getBoolean(R.styleable.ExtendedTextView_trimmed, false);
                mustValidEmail = ta.getBoolean(R.styleable.ExtendedTextView_validEmail, false);
                mMinLength = ta.getInteger(R.styleable.ExtendedTextView_minLength, 0);
                mMatchValueOf = ta.getResourceId(R.styleable.ExtendedTextView_matchValueOf, 0);
                ta.recycle();
                setFontFace(fontName, fontFormat != null ? fontFormat : FORMAT_TTF);
            } else {
                isRequired = false;
                isTrimmed = false;
                mustValidEmail = false;
                mMinLength = 0;
                mMatchValueOf = 0;
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
        this.mFontName = fontName;
        this.mFontFormat = fontFormat;
        BaseApplication app = ((BaseApplication) getContext().getApplicationContext());
        MemoryStorage<Typeface> fontCache = app.getFontCache();
        if (mFontName != null) {
            try {
                Typeface tf = fontCache.get(mFontName);
                if (tf == null) {
                    String fontFilePath = String.format("fonts/%s.%s", mFontName, mFontFormat);
                    tf = Typeface.createFromAsset(getContext().getAssets(), fontFilePath);
                    fontCache.put(mFontName, tf);
                }
                setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public boolean isTrimmed() {
        return isTrimmed;
    }

    public void setTrimmed(boolean isTrimmed) {
        this.isTrimmed = isTrimmed;
    }

    public boolean isMustValidEmail() {
        return mustValidEmail;
    }

    public void setMustValidEmail(boolean mustValidEmail) {
        this.mustValidEmail = mustValidEmail;
    }

    public int getMinLength() {
        return mMinLength;
    }

    public void setMinLength(int mMinLength) {
        this.mMinLength = mMinLength;
    }

    public int getMatchValueOf() {
        return mMatchValueOf;
    }

    public void setMatchValueOf(int mMatchValueOf) {
        this.mMatchValueOf = mMatchValueOf;
    }

    public String getValue() {
        return isTrimmed ? getText().toString().trim() : getText().toString();
    }

    public List<Rule> getRules() {
        Context c = getContext();
        List<Rule> rules = new ArrayList<>();
        if (isTrimmed) {
            rules.add(new Trimmed());
        }
        if (isRequired) {
            rules.add(new Required(c.getString(R.string.required_validation_message)));
        }
        if (mustValidEmail) {
            rules.add(new ValidEmail(c.getString(R.string.email_validation_message)));
        }
        if (mMinLength > 0) {
            rules.add(new MinimumLength(
                    String.format(c.getString(R.string.minlength_validation_message), mMinLength),
                    mMinLength
            ));
        }
        if (mMatchValueOf != 0) {
            TextView view = (TextView) ((Activity) getContext()).findViewById(mMatchValueOf);
            if (view != null) {
                rules.add(new Match(
                        String.format(c.getString(R.string.match_validation_message), view.getHint()),
                        view
                ));
            }
        }
        return rules;
    }
}
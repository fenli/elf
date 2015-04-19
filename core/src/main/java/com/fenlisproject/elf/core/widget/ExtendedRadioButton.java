package com.fenlisproject.elf.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.internal.widget.TintRadioButton;
import android.util.AttributeSet;

import com.fenlisproject.elf.R;
import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.MemoryStorage;

public class ExtendedRadioButton extends TintRadioButton implements FontFace, ExtendedAttributes {

    private String mFontName;
    private String mFontFormat;

    public ExtendedRadioButton(Context context) {
        this(context, null);
    }

    public ExtendedRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.radioButtonStyle);
    }

    public ExtendedRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initExtendedAttributes(attrs);
        }
    }

    public void initExtendedAttributes(AttributeSet attrs) {
        if (getContext().getApplicationContext() instanceof BaseApplication) {
            if (attrs != null) {
                TypedArray style = getContext().obtainStyledAttributes(
                        attrs, R.styleable.ExtendedTextView, 0, 0);
                String fontName = style.getString(R.styleable.ExtendedTextView_fontName);
                String fontFormat = style.getString(R.styleable.ExtendedTextView_fontFormat);
                style.recycle();
                setFontFace(fontName, fontFormat != null ? fontFormat : FontFace.FORMAT_TTF);
            }
        } else {
            throw new RuntimeException("Your application class must extends BaseApplication");
        }
    }

    @Override
    public void setFontFace(String fontName) {
        setFontFace(fontName, FontFace.FORMAT_TTF);
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
}

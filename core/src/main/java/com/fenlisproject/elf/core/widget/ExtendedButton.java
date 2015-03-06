package com.fenlisproject.elf.core.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.fenlisproject.elf.R;
import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.MemoryStorage;

public class ExtendedButton extends Button {

    public ExtendedButton(Context context) {
        super(context);
    }

    public ExtendedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFontFace(context, attrs);
    }

    public ExtendedButton(Context context, AttributeSet attrs) {
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
                    tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf");
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
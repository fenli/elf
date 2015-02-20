package org.elf.framework.core.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import org.elf.framework.R;
import org.elf.framework.core.cache.FontCache;

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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExtendedView, 0, 0);
        try {
            String fontName = ta.getString(R.styleable.ExtendedView_fontName);
            if (fontName != null) {
                Typeface tf = FontCache.getInstance().get(fontName);
                if (tf == null) {
                    tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf");
                    FontCache.getInstance().put(fontName, tf);
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
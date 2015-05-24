/*
* Copyright (C) 2015 Steven Lewi
* Copyright (C) 2014 The Android Open Source Project
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
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.TintableBackgroundView;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.support.v7.internal.widget.ThemeUtils;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.RemoteViews;

import com.fenlisproject.elf.R;

/**
 * Represents a push-button widget. Push-buttons can be
 * pressed, or clicked, by the user to perform an action.
 * <p/>
 * <p>A typical use of a push-button in an activity would be the following:
 * </p>
 * <p/>
 * <pre>
 * public class MyActivity extends Activity {
 *     protected void onCreate(Bundle icicle) {
 *         super.onCreate(icicle);
 *
 *         setContentView(R.layout.content_layout_id);
 *
 *         final Button button = (Button) findViewById(R.id.button_id);
 *         button.setOnClickListener(new View.OnClickListener() {
 *             public void onClick(View v) {
 *                 // Perform action on click
 *             }
 *         });
 *     }
 * }</pre>
 *
 * <p>However, instead of applying an {@link android.view.View.OnClickListener OnClickListener} to
 * the button in your activity, you can assign a method to your button in the XML layout,
 * using the {@link android.R.attr#onClick android:onClick} attribute. For example:</p>
 *
 * <pre>
 * &lt;Button
 *     android:layout_height="wrap_content"
 *     android:layout_width="wrap_content"
 *     android:text="@string/self_destruct"
 *     android:onClick="selfDestruct" /&gt;</pre>
 *
 * <p>Now, when a user clicks the button, the Android system calls the activity's {@code
 * selfDestruct(View)} method. In order for this to work, the method must be public and accept
 * a {@link android.view.View} as its only parameter. For example:</p>
 *
 * <pre>
 * public void selfDestruct(View view) {
 *     // Kabloey
 * }</pre>
 *
 * <p>The {@link android.view.View} passed into the method is a reference to the widget
 * that was clicked.</p>
 *
 * <h3>Button style</h3>
 *
 * <p>Every Button is styled using the system's default button background, which is often different
 * from one device to another and from one version of the platform to another. If you're not
 * satisfied with the default button style and want to customize it to match the design of your
 * application, then you can replace the button's background image with a <a
 * href="{@docRoot}guide/topics/resources/drawable-resource.html#StateList">state list drawable</a>.
 * A state list drawable is a drawable resource defined in XML that changes its image based on
 * the current state of the button. Once you've defined a state list drawable in XML, you can apply
 * it to your Button with the {@link android.R.attr#background android:background}
 * attribute. For more information and an example, see <a
 * href="{@docRoot}guide/topics/resources/drawable-resource.html#StateList">State List
 * Drawable</a>.</p>
 *
 * <p>See the <a href="{@docRoot}guide/topics/ui/controls/button.html">Buttons</a>
 * guide.</p>
 *
 * <p><strong>XML attributes</strong></p>
 * <p>
 * See {@link android.R.styleable#Button Button Attributes},
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 * </p>
 */
@RemoteViews.RemoteView
public class ExtendedButton extends ExtendedTextView implements TintableBackgroundView {

    private static final int[] TINT_ATTRS = {
            android.R.attr.background
    };

    private TintInfo mBackgroundTint;

    public ExtendedButton(Context context) {
        this(context, null);
    }

    public ExtendedButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public ExtendedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (TintManager.SHOULD_BE_USED) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    TINT_ATTRS, defStyleAttr, 0);
            if (a.hasValue(0)) {
                ColorStateList tint = a.getTintManager().getTintList(a.getResourceId(0, -1));
                if (tint != null) {
                    setSupportBackgroundTintList(tint);
                }
            }
            a.recycle();
        }

        // First read the TextAppearance style id
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView,
                defStyleAttr, 0);
        final int ap = a.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, -1);
        a.recycle();

        // Now check TextAppearance's textAllCaps value
        if (ap != -1) {
            TypedArray appearance = context.obtainStyledAttributes(ap, R.styleable.TextAppearance);
            if (appearance.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
            }
            appearance.recycle();
        }

        // Now read the style's value
        a = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView, defStyleAttr, 0);
        if (a.hasValue(R.styleable.AppCompatTextView_textAllCaps)) {
            setAllCaps(a.getBoolean(R.styleable.AppCompatTextView_textAllCaps, false));
        }
        a.recycle();

        final ColorStateList textColors = getTextColors();
        if (textColors != null && !textColors.isStateful()) {
            // If we have a ColorStateList which isn't stateful, create one which includes
            // a disabled state

            final int disabledTextColor;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // Pre-Lollipop, we will use textColorSecondary with android:disabledAlpha
                // applied
                disabledTextColor = ThemeUtils.getDisabledThemeAttrColor(context,
                        android.R.attr.textColorSecondary);
            } else {
                // With certain styles on Lollipop, there is a StateListAnimator which sets
                // an alpha on the whole view, so we don't need to apply disabledAlpha to
                // textColorSecondary
                disabledTextColor = ThemeUtils.getThemeAttrColor(context,
                        android.R.attr.textColorSecondary);
            }

            setTextColor(ThemeUtils.createDisabledStateList(
                    textColors.getDefaultColor(), disabledTextColor));
        }
    }


    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintList(android.view.View,
     * android.content.res.ColorStateList)}
     *
     * @hide
     */
    @Override
    public void setSupportBackgroundTintList(@Nullable ColorStateList tint) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintList = tint;
        mBackgroundTint.mHasTintList = true;

        applySupportBackgroundTint();
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintList(android.view.View)}
     *
     * @hide
     */
    @Override
    @Nullable
    public ColorStateList getSupportBackgroundTintList() {
        return mBackgroundTint != null ? mBackgroundTint.mTintList : null;
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintMode(android.view.View, android.graphics.PorterDuff.Mode)}
     *
     * @hide
     */
    @Override
    public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintMode = tintMode;
        mBackgroundTint.mHasTintMode = true;

        applySupportBackgroundTint();
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintMode(android.view.View)}
     *
     * @hide
     */
    @Override
    @Nullable
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return mBackgroundTint != null ? mBackgroundTint.mTintMode : null;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        applySupportBackgroundTint();
    }

    private void applySupportBackgroundTint() {
        if (getBackground() != null && mBackgroundTint != null) {
            TintManager.tintViewBackground(this, mBackgroundTint);
        }
    }


    public void setAllCaps(boolean allCaps) {
        setTransformationMethod(allCaps ? new AllCapsTransformationMethod(getContext()) : null);
    }

    @Override
    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        TypedArray appearance = context.obtainStyledAttributes(resId, R.styleable.TextAppearance);
        if (appearance.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        appearance.recycle();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(Button.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Button.class.getName());
    }
}
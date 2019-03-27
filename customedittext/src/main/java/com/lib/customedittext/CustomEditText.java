package com.lib.customedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * @author Kailash Chouhan
 */

public class CustomEditText extends FrameLayout {

    private static String TAG = CustomEditText.class.getSimpleName();
    private TextView tv_error, tv_toggle_password;
    private TextInputEditText editText;
    private ImageView img_left;
    float defaultLabelSize;
    private int inputType;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private int indicatorBackgroundResId = 0;
    private int drawableId = 0;
    private int cursorDrawable = 0;
    private String hintText;
    private int textColor = DEFAULT_BORDER_COLOR;
    private int toggleTextColor = DEFAULT_BORDER_COLOR;
    private int textHintColor = 0;
    private int bgTintColor = Color.WHITE;
    private int imageColor = 0;
    private int maxLines, maxLength, lines, gravity;
    private float mTextSize;
    private boolean isEditable = true;
    private int imeOptions;
    private int padding;
    private int padding_start, padding_end, padding_top, padding_bottom;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.widget_phone_layout, this, true);

        tv_error = view.findViewById(R.id.tv_error);
        tv_error.setVisibility(GONE);

        tv_toggle_password = view.findViewById(R.id.tv_toggle_password);
        tv_toggle_password.setVisibility(GONE);
        tv_toggle_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassword();
            }
        });

        img_left = view.findViewById(R.id.img_left);
        img_left.setVisibility(GONE);

        editText = view.findViewById(R.id.edt_phone);

        TypedArray styleable = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditText,
                defStyleAttr,
                0
        );

        defaultLabelSize = getResources().getDimension(R.dimen._15ssp);
        mTextSize = styleable.getDimensionPixelSize(R.styleable.CustomEditText_edt_text_size,
                DimensionsUtils.getDimensionPixelSize(context, R.dimen._13ssp));
        indicatorBackgroundResId = styleable.getResourceId(R.styleable.CustomEditText_edt_background,
                0);
        hintText = styleable.getString(R.styleable.CustomEditText_edt_hint);
        textColor = styleable.getColor(R.styleable.CustomEditText_edt_text_color, DEFAULT_BORDER_COLOR);
        toggleTextColor = styleable.getColor(R.styleable.CustomEditText_edt_toggle_text_color, DEFAULT_BORDER_COLOR);
        textHintColor = styleable.getColor(R.styleable.CustomEditText_edt_text_hint_color, 0);
        bgTintColor = styleable.getColor(R.styleable.CustomEditText_edt_background_tint, 0);
        isEditable = styleable.getBoolean(R.styleable.CustomEditText_edt_editable, true);
        maxLength = styleable.getInteger(R.styleable.CustomEditText_android_maxLength, 10000);
        maxLines = styleable.getInteger(R.styleable.CustomEditText_android_maxLines, 100);
        imeOptions = styleable.getInt(R.styleable.CustomEditText_android_imeOptions, 0);
        inputType = styleable.getInt(R.styleable.CustomEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
        lines = styleable.getInt(R.styleable.CustomEditText_android_lines, 1);
        gravity = styleable.getInt(R.styleable.CustomEditText_android_gravity, Gravity.START);
        boolean togglePassword = styleable.getBoolean(R.styleable.CustomEditText_edt_password_toggle, false);
        padding = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding, 0);
        padding_start = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_start, 0);
        padding_end = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_end, 0);
        padding_top = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_top, 0);
        padding_bottom = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_bottom, 0);

        cursorDrawable = styleable.getResourceId(R.styleable.CustomEditText_edt_cursor, 0);

        imageColor = styleable.getColor(R.styleable.CustomEditText_edt_image_tint, DEFAULT_BORDER_COLOR);
        if (togglePassword) {
            showPasswordToggle();
        }
        drawableId = styleable.getResourceId(R.styleable.CustomEditText_edt_drawable_start,
                0);
        boolean displayStartDrawable = styleable.getBoolean(R.styleable.CustomEditText_edt_show_drawable, false);
        if (displayStartDrawable) {
            showDrawable();
        }
        styleable.recycle();

        initView();
    }

    private void initView() {
        if (indicatorBackgroundResId != 0) {
            editText.setBackgroundResource(indicatorBackgroundResId);
        }
        editText.setHint(hintText);
        editText.setTextColor(textColor);
        if (textHintColor != 0) {
            editText.setHintTextColor(textHintColor);
        }
        if (bgTintColor != 0) {
            editText.getBackground().setColorFilter(bgTintColor, PorterDuff.Mode.SRC_ATOP);
        }
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        editText.setLines(lines);
        editText.setGravity(gravity);
        if (padding != 0) {
            setPadding(padding);
        } else {
            setPadding(padding_start, padding_top, padding_end, padding_bottom);
        }
        if (imageColor != 0) {
            img_left.setColorFilter(imageColor);
        }
        if (cursorDrawable != 0) {
            try {
                // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editText, cursorDrawable);
            } catch (Exception ignored) {
            }
        }
        tv_toggle_password.setTextColor(toggleTextColor);
        setInputType(inputType);
        setImeOptions(imeOptions);
        setEditable(isEditable);
        setMaxLines(maxLines);
        setMaxLength(maxLength);
        setTypeface();

    }

    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                    Log.e(TAG, "Loaded '" + assetPath);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    private void setTypeface() {
        Typeface font = get(getContext(), "fonts/sf_ui_text_regular.otf");
        editText.setTypeface(font);
        tv_toggle_password.setTypeface(font);
        tv_error.setTypeface(font);
    }

    public void setTypeface(Typeface font) {
        editText.setTypeface(font);
        tv_toggle_password.setTypeface(font);
        tv_error.setTypeface(font);
    }

    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setBackground(Drawable drawable) {
        editText.setBackground(drawable);
    }

    public void setTextColor(int color) {
        editText.setTextColor(color);
    }

    public void setSelection() {
        if (editText.getText().toString().length() > 0)
            Selection.setSelection(editText.getText(), editText.getText().length());
    }

    public void setTextSize(float textSize) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setFilters(InputFilter[] filters) {
        editText.setFilters(filters);
    }

    public void setInputType(int inputType) {
        setInputType(inputType, false);
    }

    public void setInputType(int inputType, boolean isPassword) {
        editText.setInputType(inputType);
        if (isPassword)
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void showDrawable() {
        img_left.setVisibility(VISIBLE);
        img_left.setImageResource(drawableId);
    }

    public void showPasswordToggle() {
        tv_toggle_password.setVisibility(VISIBLE);
        tv_toggle_password.setSelected(true);
    }

    public void setEditable(boolean isEditable) {
        editText.setFocusable(isEditable);
        editText.setFocusableInTouchMode(isEditable);
    }

    public void togglePassword() {
        if (tv_toggle_password.isSelected()) {
            tv_toggle_password.setSelected(false);
            tv_toggle_password.setText(getContext().getString(R.string.label_hide));
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            tv_toggle_password.setSelected(true);
            tv_toggle_password.setText(getContext().getString(R.string.label_show));
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        setSelection();
    }


    public void setPadding(int padding) {
        setPadding(padding, padding, padding, padding);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        editText.setPadding(left, top, right, bottom);
    }

    public TextView getHintTextView() {
        return tv_error;
    }

    public TextInputEditText getEditText() {
        return editText;
    }

    public void setText(CharSequence charSequence) {
        editText.setText(charSequence);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setErrorEnabled(boolean isErrorEnable) {
        tv_error.setVisibility(isErrorEnable ? VISIBLE : GONE);
    }

    public void setError(@Nullable String errorText) {
        if (isNullOrEmpty(errorText)) {
            tv_error.setVisibility(GONE);
        } else {
            tv_error.setVisibility(VISIBLE);
            tv_error.setText(errorText);
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int i, int i1, int i2, int i3) {
        editText.setCompoundDrawablesWithIntrinsicBounds(i, i1, i2, i3);
    }

    public void clearText() {
        editText.getText().clear();
    }

    public void setErrorPadding(int left, int top, int right, int bottom) {
        tv_error.setPadding(left, top, right, bottom);
    }

    /**
     * Change the editor type integer associated with the text view, which
     * is reported to an Input Method Editor (IME) with when it has focus.
     */
    public void setImeOptions(int imeOptions) {
        editText.setImeOptions(imeOptions);
    }

    public void setImeActionLabel(String label, int imeOptions) {
        editText.setImeActionLabel(label, imeOptions);
    }

    public void setOnClickListener(final OnClickListener listener) {
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty.
     * Null means to use the normal empty text. The hint does not currently
     * participate in determining the size of the view.
     */
    public final void setHint(String hint) {
        editText.setHint(hint);
    }

    public final void setHint(SpannableStringBuilder hint) {
        editText.setHint(hint);
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty,
     * from a resource.
     */
    public final void setHint(int resid) {
        setHint(getContext().getResources().getString(resid));
    }

    public void setOnEditorActionListener(final TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                listener.onEditorAction(v, actionId, event);
                return false;
            }
        });
    }

    public void setLines(int lines) {
        editText.setSingleLine(false);
        editText.setLines(lines);
    }

    public void setMaxLines(int maxLines) {
        editText.setMaxLines(maxLines);
    }

    public void setGravity(int gravity) {
        editText.setGravity(gravity);
    }

    public static boolean isNullOrEmpty(String s) {
        //return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null"));
        if (s == null)
            return true;
        if (s.length() == 0)
            return true;
        return s.equalsIgnoreCase("null");

    }
}

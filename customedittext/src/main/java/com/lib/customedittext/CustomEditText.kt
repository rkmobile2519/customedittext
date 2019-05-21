package com.lib.customedittext

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputEditText
import android.text.InputFilter
import android.text.Selection
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import java.util.*

/**
 * @author Kailash Chouhan
 */

@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate", "unused")
class CustomEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    val hintTextView: TextView
    private val tv_toggle_password: TextView
    val editText: TextInputEditText
    private val img_left: ImageView
    internal var defaultLabelSize: Float = 0.toFloat()
    private val inputType: Int
    private var indicatorBackgroundResId = 0
    private var drawableId = 0
    private var cursorDrawable = 0
    private val hintText: String?
    private var textColor = DEFAULT_BORDER_COLOR
    private var toggleTextColor = DEFAULT_BORDER_COLOR
    private var textHintColor = 0
    private var bgTintColor = Color.WHITE
    private var imageColor = 0
    private val maxLines: Int
    private val maxLength: Int
    private val lines: Int
    private val gravity: Int
    private val mTextSize: Float
    private var isEditable = true
    private val imeOptions: Int
    private val padding: Int
    private val padding_start: Int
    private val padding_end: Int
    private val padding_top: Int
    private val padding_bottom: Int

    val text: String
        get() = editText.text!!.toString().trim { it <= ' ' }

    init {

        val view = LayoutInflater.from(context).inflate(R.layout.widget_phone_layout, this, true)

        hintTextView = view.findViewById(R.id.tv_error)
        hintTextView.visibility = View.GONE

        tv_toggle_password = view.findViewById(R.id.tv_toggle_password)
        tv_toggle_password.visibility = View.GONE
        tv_toggle_password.setOnClickListener { togglePassword() }

        img_left = view.findViewById(R.id.img_left)
        img_left.visibility = View.GONE

        editText = view.findViewById(R.id.edt_phone)

        val styleable = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditText,
                defStyleAttr,
                0
        )

        defaultLabelSize = resources.getDimension(R.dimen._15ssp)
        mTextSize = styleable.getDimensionPixelSize(R.styleable.CustomEditText_edt_text_size,
                DimensionsUtils.getDimensionPixelSize(context, R.dimen._13ssp)).toFloat()
        indicatorBackgroundResId = styleable.getResourceId(R.styleable.CustomEditText_edt_background,
                0)
        hintText = styleable.getString(R.styleable.CustomEditText_edt_hint)
        textColor = styleable.getColor(R.styleable.CustomEditText_edt_text_color, DEFAULT_BORDER_COLOR)
        toggleTextColor = styleable.getColor(R.styleable.CustomEditText_edt_toggle_text_color, DEFAULT_BORDER_COLOR)
        textHintColor = styleable.getColor(R.styleable.CustomEditText_edt_text_hint_color, 0)
        bgTintColor = styleable.getColor(R.styleable.CustomEditText_edt_background_tint, 0)
        isEditable = styleable.getBoolean(R.styleable.CustomEditText_edt_editable, true)
        maxLength = styleable.getInteger(R.styleable.CustomEditText_android_maxLength, 10000)
        maxLines = styleable.getInteger(R.styleable.CustomEditText_android_maxLines, 100)
        imeOptions = styleable.getInt(R.styleable.CustomEditText_android_imeOptions, 0)
        inputType = styleable.getInt(R.styleable.CustomEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
        lines = styleable.getInt(R.styleable.CustomEditText_android_lines, 1)
        gravity = styleable.getInt(R.styleable.CustomEditText_android_gravity, Gravity.START)
        val togglePassword = styleable.getBoolean(R.styleable.CustomEditText_edt_password_toggle, false)
        padding = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding, 0)
        padding_start = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_start, 0)
        padding_end = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_end, 0)
        padding_top = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_top, 0)
        padding_bottom = styleable.getDimensionPixelOffset(R.styleable.CustomEditText_edt_padding_bottom, 0)

        cursorDrawable = styleable.getResourceId(R.styleable.CustomEditText_edt_cursor, 0)

        imageColor = styleable.getColor(R.styleable.CustomEditText_edt_image_tint, DEFAULT_BORDER_COLOR)
        if (togglePassword) {
            showPasswordToggle()
        }
        drawableId = styleable.getResourceId(R.styleable.CustomEditText_edt_drawable_start,
                0)
        val displayStartDrawable = styleable.getBoolean(R.styleable.CustomEditText_edt_show_drawable, false)
        if (displayStartDrawable) {
            showDrawable()
        }
        styleable.recycle()

        initView()
    }

    private fun initView() {
        if (indicatorBackgroundResId != 0) {
            editText.setBackgroundResource(indicatorBackgroundResId)
        }
        editText.hint = hintText
        editText.setTextColor(textColor)
        if (textHintColor != 0) {
            editText.setHintTextColor(textHintColor)
        }
        if (bgTintColor != 0) {
            editText.background.setColorFilter(bgTintColor, PorterDuff.Mode.SRC_ATOP)
        }
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        editText.setLines(lines)
        editText.gravity = gravity
        if (padding != 0) {
            setPadding(padding)
        } else {
            setPadding(padding_start, padding_top, padding_end, padding_bottom)
        }
        if (imageColor != 0) {
            img_left.setColorFilter(imageColor)
        }
        if (cursorDrawable != 0) {
            try {
                // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f.set(editText, cursorDrawable)
            } catch (ignored: Exception) {
            }

        }
        tv_toggle_password.setTextColor(toggleTextColor)
        setInputType(inputType)
        setImeOptions(imeOptions)
        setEditable(isEditable)
        setMaxLines(maxLines)
        setMaxLength(maxLength)
        setTypeface()

    }

    private fun setTypeface() {
        val font = get(context, "fonts/sf_ui_text_regular.otf")
        editText.typeface = font
        tv_toggle_password.typeface = font
        hintTextView.typeface = font
    }

    fun setTypeface(font: Typeface) {
        editText.typeface = font
        tv_toggle_password.typeface = font
        hintTextView.typeface = font
    }

    fun setMaxLength(maxLength: Int) {
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    override fun setBackground(drawable: Drawable) {
        editText.background = drawable
    }

    fun setTextColor(color: Int) {
        editText.setTextColor(color)
    }

    fun setSelection() {
        if (editText.text.toString().isNotEmpty())
            Selection.setSelection(editText.text, editText.text!!.length)
    }

    fun setTextSize(textSize: Float) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setFilters(filters: Array<InputFilter>) {
        editText.filters = filters
    }

    @JvmOverloads
    fun setInputType(inputType: Int, isPassword: Boolean = false) {
        editText.inputType = inputType
        if (isPassword)
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    fun showDrawable() {
        img_left.visibility = View.VISIBLE
        img_left.setImageResource(drawableId)
    }

    fun showPasswordToggle() {
        tv_toggle_password.visibility = View.VISIBLE
        tv_toggle_password.isSelected = true
    }

    fun setEditable(isEditable: Boolean) {
        editText.isFocusable = isEditable
        editText.isFocusableInTouchMode = isEditable
    }

    fun togglePassword() {
        if (tv_toggle_password.isSelected) {
            tv_toggle_password.isSelected = false
            tv_toggle_password.text = context.getString(R.string.label_hide)
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            tv_toggle_password.isSelected = true
            tv_toggle_password.text = context.getString(R.string.label_show)
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        setSelection()
    }


    fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        editText.setPadding(left, top, right, bottom)
    }

    fun setText(charSequence: CharSequence) {
        editText.setText(charSequence)
    }

    fun setErrorEnabled(isErrorEnable: Boolean) {
        hintTextView.visibility = if (isErrorEnable) View.VISIBLE else View.GONE
    }

    fun setError(errorText: String?) {
        if (isNullOrEmpty(errorText)) {
            hintTextView.visibility = View.GONE
        } else {
            hintTextView.visibility = View.VISIBLE
            hintTextView.text = errorText
        }
    }

    fun setCompoundDrawablesWithIntrinsicBounds(i: Int, i1: Int, i2: Int, i3: Int) {
        editText.setCompoundDrawablesWithIntrinsicBounds(i, i1, i2, i3)
    }

    fun clearText() {
        editText.text?.clear()
    }

    fun setErrorPadding(left: Int, top: Int, right: Int, bottom: Int) {
        hintTextView.setPadding(left, top, right, bottom)
    }

    /**
     * Change the editor type integer associated with the text view, which
     * is reported to an Input Method Editor (IME) with when it has focus.
     */
    fun setImeOptions(imeOptions: Int) {
        editText.imeOptions = imeOptions
    }

    fun setImeActionLabel(label: String, imeOptions: Int) {
        editText.setImeActionLabel(label, imeOptions)
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        editText.setOnClickListener { view -> listener!!.onClick(view) }
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty.
     * Null means to use the normal empty text. The hint does not currently
     * participate in determining the size of the view.
     */
    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setHint(hint: SpannableStringBuilder) {
        editText.hint = hint
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty,
     * from a resource.
     */
    fun setHint(resid: Int) {
        setHint(context.resources.getString(resid))
    }

    fun setOnEditorActionListener(listener: TextView.OnEditorActionListener) {
        editText.setOnEditorActionListener { v, actionId, event ->
            listener.onEditorAction(v, actionId, event)
            false
        }
    }

    fun setLines(lines: Int) {
        editText.setSingleLine(false)
        editText.setLines(lines)
    }

    fun setMaxLines(maxLines: Int) {
        editText.maxLines = maxLines
    }

    fun setGravity(gravity: Int) {
        editText.gravity = gravity
    }

    companion object {

        private val TAG = CustomEditText::class.java.simpleName
        private const val DEFAULT_BORDER_COLOR = Color.BLACK

        private val cache = Hashtable<String, Typeface>()

        operator fun get(c: Context, assetPath: String): Typeface? {
            synchronized(cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        val t = Typeface.createFromAsset(c.assets,
                                assetPath)
                        cache[assetPath] = t
                        Log.e(TAG, "Loaded '$assetPath")
                    } catch (e: Exception) {
                        Log.e(TAG, "Could not get typeface '" + assetPath
                                + "' because " + e.message)
                        return null
                    }

                }
                return cache[assetPath]
            }
        }

        fun isNullOrEmpty(s: String?): Boolean {
            //return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null"));
            if (s == null)
                return true
            return if (s.isEmpty()) true else s.equals("null", ignoreCase = true)

        }
    }
}

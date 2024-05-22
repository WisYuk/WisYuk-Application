package com.weynard02.wisyuk.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.weynard02.wisyuk.R
import com.weynard02.wisyuk.utils.Utils.isEmailValid

class MyEmailEditText: AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        hint = context.getString(R.string.enter_email)
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isEmailValid(s)) {
                    setError(context.getString(R.string.email_format_is_not_correct), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
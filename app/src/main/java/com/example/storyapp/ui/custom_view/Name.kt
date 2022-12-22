package com.example.storyapp.ui.custom_view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class Name: AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun validateName() {
        val name = text?.trim()
        if (name.isNullOrEmpty()) {
            error = resources.getString(R.string.name_required)
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) validateName()

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

}
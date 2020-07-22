package com.syject.eqally.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import com.syject.eqally.R

class ParallaxScrollView : ScrollView {

    private val defaultScrollFactor = 0.6f
    private var scrollFactor = defaultScrollFactor
    private var backgroundResId = 0
    private var backgroundView: View? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs) {
        initView(context, attrs, defStyle)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyle: Int) {
        if (isInEditMode) return
        if (attrs != null) {
            val values =
                context.obtainStyledAttributes(attrs, R.styleable.ParallaxScrollView, defStyle, 0)
            backgroundResId =
                values.getResourceId(R.styleable.ParallaxScrollView_backgroundView, 0)
            scrollFactor =
                values.getFloat(R.styleable.ParallaxScrollView_scrollFactor, defaultScrollFactor)
            values.recycle()
        }
        isVerticalFadingEdgeEnabled = false
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) translateBackgroundView(scrollY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        backgroundView = findViewById(backgroundResId)
        backgroundResId = 0
    }

    @Override
    override fun onDetachedFromWindow() {
        backgroundView?.clearAnimation()
        backgroundView = null
        super.onDetachedFromWindow()
    }

    @Override
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        translateBackgroundView(t)
    }

    private fun translateBackgroundView(y: Int) {
        backgroundView?.translationY = (y * scrollFactor).toInt().toFloat()
    }
}

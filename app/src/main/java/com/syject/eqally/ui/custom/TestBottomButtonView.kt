package com.syject.eqally.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.syject.eqally.R

class TestBottomButtonView : LinearLayout {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, att: AttributeSet?) : this(context, att, 0)
    constructor(context: Context, att: AttributeSet?, def: Int) : super(context, att, def) {
        initLayout()
    }

    private lateinit var repeatButtonLayout: View
    private lateinit var readyButton: View
    private lateinit var repeatButton: Button

    enum class TestState {
        NEW_TEST,
        READY,
        SHOWING_EMOTION,
        ANSWER_OR_REPEAT,
        RESULT
    }

    private fun initLayout() {
        View.inflate(context, R.layout.custom_test_bottom_panel, this)
        readyButton = findViewById(R.id.readyButton)
        repeatButtonLayout = findViewById(R.id.repeatButtonLayout)
        repeatButton = findViewById(R.id.repeatButton)
    }

    fun changeBottomPanelState(testState: TestState) {
        when (testState) {
            TestState.NEW_TEST -> {
                visibility = View.VISIBLE
                readyButton.visibility = View.VISIBLE
                repeatButtonLayout.visibility = View.GONE
            }
            TestState.READY -> {
                visibility = View.VISIBLE
                readyButton.visibility = View.VISIBLE
                repeatButtonLayout.visibility = View.GONE
                isEnabled = true
            }
            TestState.SHOWING_EMOTION -> {
                readyButton.visibility = View.GONE
                visibility = View.GONE
            }
            TestState.ANSWER_OR_REPEAT -> {
                visibility = View.VISIBLE
                repeatButtonLayout.visibility = View.VISIBLE
            }
            TestState.RESULT -> {
                repeatButtonLayout.visibility = View.GONE
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        repeatButton.alpha = if (enabled) 1f else 0.5f
        repeatButton.isEnabled = enabled
    }
}
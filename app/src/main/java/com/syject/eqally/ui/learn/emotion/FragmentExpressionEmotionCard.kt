package com.syject.eqally.ui.learn.emotion

import android.os.Bundle
import androidx.cardview.widget.CardView
import com.syject.eqally.R
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.item_learn_emotion_expression.*

class FragmentExpressionEmotionCard private constructor() : BaseFragment() {

    override fun getLayoutID() = R.layout.item_learn_emotion_expression

    companion object {

        fun getInstance(cardText: String) =
            FragmentExpressionEmotionCard().apply {
                arguments = Bundle().apply {
                    putString(Constants.CARD_TEXT, cardText)
                }
            }
    }

    override fun afterViewCreated() {
        expressionDescription.text = arguments!!.getString(Constants.CARD_TEXT)
    }

    override fun getCardView(): CardView? =
        if (fragmentView == null) null
        else fragmentView as CardView
}
package com.syject.eqally.ui.select.emotion

import android.content.Intent
import com.syject.eqally.R
import com.syject.eqally.adapters.EmotionsAdapter
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.learn.emotion.ActivityLearnEmotion
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.activity_emotion_list.*

class SelectEmotionActivity : BaseActivity() {

    override fun getLayoutID() = R.layout.activity_emotion_list

    override fun afterViewCreated() {
        emotionList.adapter = EmotionsAdapter(
            this,
            EmotionEnums.values().filter { it != EmotionEnums.NEUTRAL }.toList()
        ) {
            val intent = Intent(this, ActivityLearnEmotion::class.java)
            intent.putExtra(Constants.SELECTED_EMOTION, it)
            showNextActivity(intent)
        }
    }
}
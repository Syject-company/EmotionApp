package com.syject.eqally.ui.learn.emotion

import android.graphics.Bitmap
import android.os.Bundle
import com.syject.eqally.R
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants
import com.syject.eqally.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_lear_emotion_description.*

class FragmentDescriptionEmotion private constructor() : BaseFragment() {
    private lateinit var selectedEmotion: EmotionEnums

    companion object {

        fun getInstance(fragmentTitle: String, emotion: EmotionEnums) =
            FragmentDescriptionEmotion().apply {
                arguments = Bundle().apply {
                    putString(Constants.PAGE_TITLE, fragmentTitle)
                    putSerializable(Constants.SELECTED_EMOTION, emotion)
                }
            }
    }

    override fun getLayoutID() = R.layout.fragment_lear_emotion_description

    override fun afterViewCreated() {
        selectedEmotion = arguments!!.getSerializable(Constants.SELECTED_EMOTION) as EmotionEnums
        AppUtils.decodeImages(
            baseActivity,
            selectedEmotion.emotionFile,
            object : AppUtils.DecodeListener {
                override fun onDecode(bitmap: Bitmap) {
                    emotionImage.setImageBitmap(bitmap)
                }
            })
        emotionName.setText(selectedEmotion.emotionName)
        emotionDescription.setText(selectedEmotion.emotionDescription)
        emotionTrigger.setText(selectedEmotion.emotionTrigger)
        emotionPurpose.setText(selectedEmotion.emotionPurpose)
        emotionBody.setText(selectedEmotion.emotionBody)
        emotionFeel.setText(selectedEmotion.emotionFeel)
        emotionSound.setText(selectedEmotion.emotionSound)
    }
}
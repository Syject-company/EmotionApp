package com.syject.eqally.ui.learn.emotion

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.syject.eqally.R
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.AnimationUtils
import com.syject.eqally.utils.AppUtils
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.fragment_lear_emotion_example.*

class FragmentExampleEmotion private constructor() : BaseFragment(), View.OnTouchListener {
    private lateinit var emotionEnums: EmotionEnums

    companion object {

        fun getInstance(fragmentTitle: String, emotion: EmotionEnums) =
            FragmentExampleEmotion().apply {
                arguments = Bundle().apply {
                    putString(Constants.PAGE_TITLE, fragmentTitle)
                    putSerializable(Constants.SELECTED_EMOTION, emotion)
                }
            }
    }

    override fun getLayoutID() = R.layout.fragment_lear_emotion_example

    override fun afterViewCreated() {
        emotionEnums = arguments!!.getSerializable(Constants.SELECTED_EMOTION) as EmotionEnums
        decodeImage(EmotionEnums.NEUTRAL.emotionFile, object : AppUtils.DecodeListener {
            override fun onDecode(bitmap: Bitmap) {
                normalEmotion.setImageBitmap(bitmap)
            }
        })
        decodeImage(emotionEnums.emotionFile, object : AppUtils.DecodeListener {
            override fun onDecode(bitmap: Bitmap) {
                currentEmotion.setImageBitmap(bitmap)
            }
        })
        tapLayout.setOnTouchListener(this)
    }

    private fun decodeImage(imageFile: String, listener: AppUtils.DecodeListener) {
        AppUtils.decodeImages(baseActivity, imageFile, listener)
    }

    override fun onResume() {
        super.onResume()
        touchIcon.scaleX = 1f
        touchIcon.scaleY = 1f
        tapLayout.alpha = 1f
        touchIcon.requestLayout()
        decreaseScaleAnimation()
    }

    private fun increaseScaleAnimation() {
        AnimationUtils.scaleAnimation(touchIcon, object : AnimationUtils.OnAnimationEndListener {
            override fun onAnimationEnd() {
                when (tapLayout?.alpha) {
                    1f -> {
                        decreaseScaleAnimation()
                    }
                    else -> {
                        touchIcon.clearAnimation()
                    }
                }
            }
        }, 1f)
    }

    private fun decreaseScaleAnimation() {
        AnimationUtils.scaleAnimation(touchIcon, object : AnimationUtils.OnAnimationEndListener {
            override fun onAnimationEnd() {
                when (tapLayout?.alpha) {
                    1f -> {
                        increaseScaleAnimation()
                    }
                    else -> {
                        touchIcon.clearAnimation()
                    }
                }
            }
        }, 0.8f)
    }

    override fun onPause() {
        super.onPause()
        touchIcon?.clearAnimation()
        tapLayout?.alpha = 1f
        currentEmotion?.alpha = 0f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                tapLayout.alpha = 0f
                AnimationUtils.alphaAnimation(currentEmotion, null, 1f)
            }
            MotionEvent.ACTION_UP -> {
                AnimationUtils.alphaAnimation(currentEmotion, null, 0f)
            }
        }
        return true
    }
}
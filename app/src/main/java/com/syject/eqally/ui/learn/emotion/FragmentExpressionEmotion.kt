package com.syject.eqally.ui.learn.emotion

import android.graphics.Bitmap
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.syject.eqally.R
import com.syject.eqally.adapters.viewpages.CardOffsetViewPagerAdapter
import com.syject.eqally.data.DataManager
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.ui.custom.AchievementDialog
import com.syject.eqally.ui.custom.ShadowTransformer
import com.syject.eqally.utils.Constants
import com.syject.eqally.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_lear_emotion_expression.*


class FragmentExpressionEmotion private constructor() : BaseFragment(),
    ViewPager.OnPageChangeListener {
    private lateinit var selectedEmotion: EmotionEnums

    companion object {

        fun getInstance(fragmentTitle: String, emotion: EmotionEnums) =
            FragmentExpressionEmotion().apply {
                arguments = Bundle().apply {
                    putString(Constants.PAGE_TITLE, fragmentTitle)
                    putSerializable(Constants.SELECTED_EMOTION, emotion)
                }
            }
    }

    override fun getLayoutID() = R.layout.fragment_lear_emotion_expression

    override fun afterViewCreated() {
        selectedEmotion = arguments!!.getSerializable(Constants.SELECTED_EMOTION) as EmotionEnums
        arrowLayout.currentEmotion = selectedEmotion
        arrowLayout.attachViewPager(expressionCardsView)
        expressionCardsView.addOnPageChangeListener(this)
        AppUtils.decodeImages(
            baseActivity,
            selectedEmotion.emotionFile,
            object : AppUtils.DecodeListener {
                override fun onDecode(bitmap: Bitmap) {
                    arrowLayout.showImage(bitmap)
                }
            })
        val cardExpressionAdapter =
            CardOffsetViewPagerAdapter(childFragmentManager, getCardFragments())
        val shadowTransformer = ShadowTransformer(expressionCardsView, cardExpressionAdapter)
        shadowTransformer.enableScaling(true)
        expressionCardsView.adapter = cardExpressionAdapter
        expressionCardsView.setPageTransformer(false, shadowTransformer)
        expressionCardsView.offscreenPageLimit = cardExpressionAdapter.count
    }

    override fun onResume() {
        super.onResume()
        dataManager.checkLearnAchievement(selectedEmotion, 0)
    }

    private fun getCardFragments(): Array<BaseFragment> {
        val cardTexts = resources.getStringArray(selectedEmotion.emotionCards)
        val fragmentsList = mutableListOf<FragmentExpressionEmotionCard>()
        cardTexts.forEach { fragmentsList.add(FragmentExpressionEmotionCard.getInstance(it)) }
        return fragmentsList.toTypedArray()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (dataManager.checkLearnAchievement(selectedEmotion, position)) {
            AchievementDialog(baseActivity).showAchievement(DataManager.instance.getNewAchievement())
        }
    }
}
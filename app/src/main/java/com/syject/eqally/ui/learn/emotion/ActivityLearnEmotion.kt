package com.syject.eqally.ui.learn.emotion

import com.syject.eqally.R
import com.syject.eqally.adapters.viewpages.TabsAdapter
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.activity_learn_emotion.*

class ActivityLearnEmotion : BaseActivity() {

    override fun getLayoutID() = R.layout.activity_learn_emotion

    override fun afterViewCreated() {
        val emotion =
            intent.extras!!.getSerializable(Constants.SELECTED_EMOTION) as EmotionEnums
        pagesView.adapter = TabsAdapter(
            supportFragmentManager,
            arrayOf(
                FragmentDescriptionEmotion.getInstance(
                    getString(R.string.LearnEmotionActivity_tab_description),
                    emotion
                ),
                FragmentExpressionEmotion.getInstance(
                    getString(R.string.LearnEmotionActivity_tab_expression),
                    emotion
                ),
                FragmentExampleEmotion.getInstance(
                    getString(R.string.LearnEmotionActivity_tab_example),
                    emotion
                )
            )
        )
        tabsLayout.setupWithViewPager(pagesView)
        titleView.setText(emotion.emotionName)
        pagesView.offscreenPageLimit = pagesView.adapter!!.count
    }
}
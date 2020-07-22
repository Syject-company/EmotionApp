package com.syject.eqally.ui.result

import com.syject.eqally.R
import com.syject.eqally.adapters.viewpages.TabsAdapter
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.custom.AchievementDialog
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : BaseActivity() {

    override fun getLayoutID() = R.layout.activity_result

    override fun afterViewCreated() {
        showAchievement()
        resultViewPager.adapter = TabsAdapter(
            supportFragmentManager,
            arrayOf(
                FragmentLastResult.getInstance(getString(R.string.ResultActivity_tab_last_result)),
                FragmentAchievements.getInstance(getString(R.string.ResultActivity_tab_achievements)),
                FragmentRanking.getInstance(getString(R.string.ResultActivity_tab_ranking))
            )
        )
        resultTabLayout.setupWithViewPager(resultViewPager)
        resultViewPager.offscreenPageLimit = resultViewPager.adapter!!.count
    }

    private fun showAchievement() {
        AchievementDialog(this).showAchievement(getNewAchievement())
    }

    private fun getNewAchievement() =
        dataManager.getNewAchievement()
}
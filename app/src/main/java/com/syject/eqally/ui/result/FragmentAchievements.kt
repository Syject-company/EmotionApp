package com.syject.eqally.ui.result

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.syject.eqally.R
import com.syject.eqally.adapters.AchievementAdapter
import com.syject.eqally.data.enums.AchievementEnum
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.ui.custom.AchievementDescriptionDialog
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.fragment_result_achievements.*

class FragmentAchievements private constructor() : BaseFragment() {

    companion object {
        fun getInstance(fragmentTitle: String) =
            FragmentAchievements().apply {
                arguments = Bundle().apply { putString(Constants.PAGE_TITLE, fragmentTitle) }
            }
    }

    override fun getLayoutID() = R.layout.fragment_result_achievements

    override fun afterViewCreated() {
        achievementView.layoutManager = GridLayoutManager(baseActivity, 3)
        achievementView.adapter =
            AchievementAdapter(baseActivity, getAchievementList()) {
                AchievementDescriptionDialog(baseActivity, it!!).show()
            }
    }

    private fun getAchievementList(): List<AchievementEnum?> {
        val tempList = mutableListOf<AchievementEnum?>()
        tempList.addAll(AchievementEnum.values().toList())
        tempList.add(5, null)
        return tempList
    }
}
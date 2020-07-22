package com.syject.eqally.adapters.viewpages

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.syject.eqally.data.models.AchievementInfo


class AchievementImageDialogAdapter constructor(
    private val context: Context,
    private val achievements: List<AchievementInfo>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any) =
        view == `object`

    override fun getCount() = achievements.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setImageResource(getAchievementInfoByPosition(position).achievementEnum.imageID)
        }
        container.addView(view)
        return view
    }

    fun getAchievementInfoByPosition(position: Int) =
        achievements[position]
}
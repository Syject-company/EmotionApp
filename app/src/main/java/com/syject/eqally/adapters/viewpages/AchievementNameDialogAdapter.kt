package com.syject.eqally.adapters.viewpages

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.syject.eqally.R
import com.syject.eqally.data.models.AchievementInfo

class AchievementNameDialogAdapter constructor(
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
        val view = TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            textSize = 25f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(context, R.color.darkGreyBlue))
            setText(achievements[position].achievementEnum.achievementName)
        }
        container.addView(view)
        return view
    }
}
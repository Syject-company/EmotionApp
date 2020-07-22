package com.syject.eqally.adapters

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.syject.eqally.R
import com.syject.eqally.data.DataManager
import com.syject.eqally.data.enums.AchievementEnum
import com.syject.eqally.ui.base.BaseActivity
import java.util.*

class AchievementAdapter(
    baseActivity: BaseActivity,
    list: List<AchievementEnum?>,
    onItemClick: (AchievementEnum?) -> Unit
) : AppBaseAdapter<AchievementEnum>(baseActivity, list, onItemClick) {

    override fun isCallClick(item: AchievementEnum?, position: Int) = item != null

    override fun getLayoutId() = R.layout.item_result_achievement

    override fun createHolder(inflate: View) = object : BaseItem<AchievementEnum>(inflate) {
        val achievementImage = inflate.findViewById<ImageView>(R.id.achievementImage)
        val achievementDate = inflate.findViewById<TextView>(R.id.achievementDate)
        val achievementName = inflate.findViewById<TextView>(R.id.achievementName)
        val achievementTimes = inflate.findViewById<TextView>(R.id.achievementTimes)

        @SuppressLint("SetTextI18n")
        override fun bind(t: AchievementEnum) {
            val achievementInfo = DataManager.instance.getAchievementInfo(t)
            val achievementIsTaken = achievementInfo != null &&
                    achievementInfo.achievementTimeStamp > 0
            if (achievementIsTaken) inflate.alpha = 1f
            else inflate.alpha = 0.3f
            achievementImage.setImageResource(t.imageID)
            achievementName.setText(t.achievementName)
            if (achievementIsTaken) {
                achievementDate.text = getAchievementData(achievementInfo!!.achievementTimeStamp)
                achievementTimes.text =
                    "${achievementInfo.countTakes / achievementInfo.achievementEnum.achievementRowCount} " +
                            context.resources.getString(R.string.Achievement_time_s)
            }
        }
    }

    fun getAchievementData(timeStamp: Long) =
        DateFormat.format("dd MMM yyyy", Calendar.getInstance().apply {
            timeInMillis = timeStamp
        }).toString()
}
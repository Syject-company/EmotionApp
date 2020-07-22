package com.syject.eqally.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.syject.eqally.R
import com.syject.eqally.data.enums.ScreensEnums
import com.syject.eqally.ui.base.BaseActivity

class ScreensAdapter(
    baseActivity: BaseActivity,
    list: List<ScreensEnums>,
    onItemClick: (ScreensEnums) -> Unit
) : AppBaseAdapter<ScreensEnums>(baseActivity, list, onItemClick) {

    override fun isCallClick(item: ScreensEnums?, position: Int) = true

    override fun getLayoutId() = R.layout.item_splash_menu

    override fun createHolder(inflate: View) = object : BaseItem<ScreensEnums>(inflate) {
        val screenIcon = inflate.findViewById<ImageView>(R.id.screen_icon)
        val screenName = inflate.findViewById<TextView>(R.id.screen_name)
        val screenDivider = inflate.findViewById<View>(R.id.divider)

        override fun bind(t: ScreensEnums) {
            screenIcon.setImageResource(t.imageID)
            screenName.setText(t.screenName)
            screenDivider.visibility =
                if (adapterPosition == 0) View.GONE
                else View.VISIBLE
        }
    }
}
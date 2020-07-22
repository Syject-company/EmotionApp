package com.syject.eqally.adapters

import android.view.View
import android.widget.TextView
import com.syject.eqally.R
import com.syject.eqally.data.enums.MoreTabsEnums
import com.syject.eqally.ui.base.BaseActivity

class MoreAdapter(
    baseActivity: BaseActivity,
    list: List<MoreTabsEnums>,
    onItemClick: (MoreTabsEnums) -> Unit
) : AppBaseAdapter<MoreTabsEnums>(baseActivity, list, onItemClick) {

    override fun isCallClick(item: MoreTabsEnums?, position: Int) = true

    override fun getLayoutId() = R.layout.item_more_tab

    override fun createHolder(inflate: View) = object : BaseItem<MoreTabsEnums>(inflate) {
        val tabsName = inflate.findViewById<TextView>(R.id.tabName)
        override fun bind(t: MoreTabsEnums) {
            tabsName.setText(t.tabName)
        }
    }
}
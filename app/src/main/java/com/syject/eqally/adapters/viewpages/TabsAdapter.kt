package com.syject.eqally.adapters.viewpages

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants

class TabsAdapter
constructor(fragmentManager: FragmentManager, private val fragments: Array<BaseFragment>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) =
        fragments[position].arguments!!.getString(Constants.PAGE_TITLE)!!
}
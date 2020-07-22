package com.syject.eqally.adapters.viewpages

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.syject.eqally.ui.base.BaseFragment


class CardOffsetViewPagerAdapter
constructor(
    fragmentManager: FragmentManager,
    private val fragments: Array<BaseFragment>
) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    val maxElevationFactor = 3f
    val baseElevation = 2f

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    fun getCardViewAt(position: Int): CardView? = fragments[position].getCardView()

    override fun instantiateItem(container: ViewGroup, position: Int): BaseFragment {
        val fragment = super.instantiateItem(container, position)
        fragments[position] = fragment as BaseFragment
        return fragment
    }
}

package com.syject.eqally.ui.custom

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class OnPageChangeListenerSync(private val master: ViewPager, private val slave: ViewPager) :
    OnPageChangeListener {

    private var mScrollState = ViewPager.SCROLL_STATE_IDLE
    override fun onPageScrolled(        position: Int,        positionOffset: Float,        positionOffsetPixels: Int
    ) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) return
        slave.scrollTo(master.scrollX * slave.width / master.width, 0)
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {
        mScrollState = state
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            slave.setCurrentItem(master.currentItem, false)
        }
    }
}
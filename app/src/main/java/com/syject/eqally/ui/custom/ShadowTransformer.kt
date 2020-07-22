package com.syject.eqally.ui.custom

import android.view.View
import androidx.cardview.widget.CardView

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.syject.eqally.adapters.viewpages.CardOffsetViewPagerAdapter


class ShadowTransformer(
    private val viewPager: ViewPager,
    private val adapter: CardOffsetViewPagerAdapter
) : OnPageChangeListener, ViewPager.PageTransformer {
    private var lastOffset = 0f
    private var scalingEnabled = false

    init {
        viewPager.addOnPageChangeListener(this)
    }

    fun enableScaling(enable: Boolean) {
        val currentCard: CardView = adapter.getCardViewAt(viewPager.currentItem) ?: return
        if (scalingEnabled && !enable) {
            currentCard.animate().scaleY(1f)
            currentCard.animate().scaleX(1f)
        } else if (!scalingEnabled && enable) {
            currentCard.animate().scaleY(1.1f)
            currentCard.animate().scaleX(1.1f)
        }
        scalingEnabled = enable
    }

    override fun transformPage(page: View, position: Float) {}

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        val realCurrentPosition: Int
        val nextPosition: Int
        val baseElevation: Float = adapter.baseElevation
        val realOffset: Float
        val goingLeft = lastOffset > positionOffset
        if (goingLeft) {
            realCurrentPosition = position + 1
            nextPosition = position
            realOffset = 1 - positionOffset
        } else {
            nextPosition = position + 1
            realCurrentPosition = position
            realOffset = positionOffset
        }
        if (nextPosition > adapter.count - 1 || realCurrentPosition > adapter.count - 1) return
        val currentCard: CardView = adapter.getCardViewAt(realCurrentPosition) ?: return
        if (scalingEnabled) {
            currentCard.scaleX = (1 + 0.1 * (1 - realOffset)).toFloat()
            currentCard.scaleY = (1 + 0.1 * (1 - realOffset)).toFloat()
        }
        currentCard.cardElevation =
            baseElevation + (baseElevation * (adapter.maxElevationFactor - 1) * (1 - realOffset))
        val nextCard: CardView = adapter.getCardViewAt(nextPosition) ?: return
        if (scalingEnabled) {
            nextCard.scaleX = (1 + 0.1 * realOffset).toFloat()
            nextCard.scaleY = (1 + 0.1 * realOffset).toFloat()
        }
        nextCard.cardElevation =
            baseElevation + (baseElevation * (adapter.maxElevationFactor - 1) * realOffset)
        lastOffset = positionOffset
    }

    override fun onPageSelected(position: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}
}
package com.syject.eqally.ui.app.test

import android.view.View
import android.view.WindowManager
import com.syject.eqally.R
import com.syject.eqally.adapters.viewpages.CardOffsetViewPagerAdapter
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.ui.custom.ShadowTransformer
import com.syject.eqally.utils.BillingUtils
import kotlinx.android.synthetic.main.activity_take_test.*

class ActivityTakeTest : BaseActivity(), View.OnClickListener {

    override fun getLayoutID() = R.layout.activity_take_test

    override fun afterViewCreated() {
        showTests()
        restorePurchasesButton.setOnClickListener(this)
    }

    private fun showTests() {
        val cardOffsetAdapter =
            CardOffsetViewPagerAdapter(supportFragmentManager, getCardFragments())
        val shadowTransformer = ShadowTransformer(testsViewPages, cardOffsetAdapter)
        shadowTransformer.enableScaling(true)
        testsViewPages.adapter = cardOffsetAdapter
        testsViewPages.setPageTransformer(false, shadowTransformer)
        testsViewPages.offscreenPageLimit = cardOffsetAdapter.count
    }

    private fun getCardFragments(): Array<BaseFragment> {
        val fragmentsList = mutableListOf<FragmentTestCard>()
        TestEnums.values().forEach { fragmentsList.add(FragmentTestCard.getInstance(it)) }
        return fragmentsList.toTypedArray()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.restorePurchasesButton -> {
                BillingUtils.instance.restorePurchases()
            }
        }
    }

    override fun beforeViewCreated() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}
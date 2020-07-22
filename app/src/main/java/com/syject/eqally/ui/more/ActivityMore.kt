package com.syject.eqally.ui.more

import androidx.fragment.app.FragmentTransaction
import com.syject.eqally.R
import com.syject.eqally.data.enums.MoreTabsEnums
import com.syject.eqally.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_more.*

class ActivityMore : BaseActivity() {

    override fun getLayoutID() = R.layout.activity_more

    override fun afterViewCreated() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FragmentMoreRoot())
            .commit()
    }

    override fun setTitle(titleID: Int) {
        titleView.setText(titleID)
    }

    fun showMoreTab(moreTab: MoreTabsEnums) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_right_to_left,
                R.anim.exit_right_to_left,
                R.anim.enter_left_to_right,
                R.anim.exit_left_to_right
            )
            .replace(R.id.fragmentContainer, FragmentMoreSelectedTab.getInstance(moreTab))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
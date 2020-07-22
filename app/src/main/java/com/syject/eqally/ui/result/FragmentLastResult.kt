package com.syject.eqally.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.syject.eqally.R
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.ui.app.test.ActivityTakeTest
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.fragment_result_last_result.*

class FragmentLastResult private constructor() : BaseFragment(), View.OnClickListener {

    companion object {
        fun getInstance(fragmentTitle: String) =
            FragmentLastResult().apply {
                arguments = Bundle().apply { putString(Constants.PAGE_TITLE, fragmentTitle) }
            }
    }

    override fun getLayoutID() = R.layout.fragment_result_last_result

    override fun afterViewCreated() {
        takeTestButton.setOnClickListener(this)
        buyTestButton.setOnClickListener(this)
        dataManager.getLastTestResultModel()?.let {
            realScore.text = it.getRealScope()
            correctAnswer.text = it.getCorrectAnswerString()
            replyCountTextView.text = "${it.replaysCount}"
            emptyResultLayout.visibility = View.GONE
            resultLayout.visibility = View.VISIBLE
        }
        if (hasPaidTest())
            buyLayout.visibility = View.GONE
    }

    private fun hasPaidTest(): Boolean {
        return TestEnums
            .values()
            .any { testEnums -> testEnums != TestEnums.DEMO && dataManager.isTestPaid(testEnums) }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.takeTestButton, R.id.buyTestButton -> {
                showTakeTestActivity()
            }
        }
    }

    private fun showTakeTestActivity() {
        baseActivity.showNextActivity(Intent(baseActivity, ActivityTakeTest::class.java))
    }
}
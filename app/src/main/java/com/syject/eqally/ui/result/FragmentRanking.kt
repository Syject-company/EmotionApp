package com.syject.eqally.ui.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.syject.eqally.R
import com.syject.eqally.adapters.RankingAdapter
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.RankingTestResultModel
import com.syject.eqally.ui.app.test.ActivityTakeTest
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants
import com.syject.eqally.utils.GSONUtils
import kotlinx.android.synthetic.main.content_result_ranking_registrate.*
import kotlinx.android.synthetic.main.fragment_result_ranking.*
import okhttp3.Response
import org.json.JSONObject


class FragmentRanking private constructor() : BaseFragment(), View.OnClickListener {
    private lateinit var rankingAdapter: RankingAdapter
    private lateinit var lastsUserTestsForRankingModel: List<RankingTestResultModel>

    companion object {
        fun getInstance(fragmentTitle: String) =
            FragmentRanking().apply {
                arguments = Bundle().apply { putString(Constants.PAGE_TITLE, fragmentTitle) }
            }
    }

    override fun getLayoutID() = R.layout.fragment_result_ranking

    override fun afterViewCreated() {
        takeTestButton.setOnClickListener(this)
        joinButton.setOnClickListener(this)
        rankingAdapter = RankingAdapter(baseActivity) { }
        rankingView.adapter = rankingAdapter
        if (hasPaidTest()) showRankingIfUserHasPaid()
    }

    private fun showRankingIfUserHasPaid() {
        takeTestLayout.visibility = View.GONE
        lastsUserTestsForRankingModel = dataManager.getRankingTests()
        dataManager.getLastTestResultModel()?.let {
            if (it.test == TestEnums.DEMO) {
                demoTestText.visibility = View.VISIBLE
            }
        }
        if (lastsUserTestsForRankingModel.isNotEmpty()) {
            if (dataManager.isUserRegister()) {
                if (!baseActivity.intent.extras!!.getBoolean(Constants.IS_FROM_MAIN_SCREEN)) {
                    sendUserLastsTests()
                    return
                }
            } else {
                showRegistrationLayout()
            }
        }
        loadRanking()
    }

    private fun showRegistrationLayout() {
        registrLayout.visibility = View.VISIBLE
        rankingView.alpha = 0.3f
    }

    private fun hideRegistrationLayout() {
        registrLayout.visibility = View.GONE
        rankingView.alpha = 1f
    }

    private fun sendUserLastsTests() {
        dataManager.sendUserLastsTests(lastsUserTestsForRankingModel)
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    rankingAdapter.attachRanking(GSONUtils.fromJsonToObject(response.toString())!!)
                }

                override fun onError(anError: ANError) {
                }
            })
    }

    private fun loadRanking() {
        dataManager
            .getRanking()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    rankingAdapter.attachRanking(GSONUtils.fromJsonToObject(response.toString())!!)
                }

                override fun onError(anError: ANError) {

                }
            })
    }

    private fun hasPaidTest(): Boolean {
        return TestEnums
            .values()
            .any { testEnums -> testEnums != TestEnums.DEMO && dataManager.isTestPaid(testEnums) }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.takeTestButton -> {
                baseActivity.showNextActivity(Intent(baseActivity, ActivityTakeTest::class.java))
                baseActivity.finish()
            }
            R.id.joinButton -> {
                hideKeyboard()
                registrateUser()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    private fun registrateUser() {
        val userName = userNameEditText.text.toString()
        if (userName.isEmpty()) {
            return
        }
        dataManager
            .registerUser(userName)
            .getAsOkHttpResponse(object : OkHttpResponseListener {
                override fun onResponse(response: Response) {
                    if (response.isSuccessful) {
                        hideRegistrationLayout()
                        dataManager.setUserIsRegister()
                        sendUserLastsTests()
                    }
                }

                override fun onError(anError: ANError) {
                }
            })
    }

    fun hideKeyboard() {
        val imm =
            baseActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = baseActivity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
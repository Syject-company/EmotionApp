package com.syject.eqally.ui

import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.jaeger.library.StatusBarUtil
import com.syject.eqally.R
import com.syject.eqally.adapters.ScreensAdapter
import com.syject.eqally.data.enums.ScreensEnums
import com.syject.eqally.ui.app.test.ActivityTakeTest
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.more.ActivityMore
import com.syject.eqally.ui.result.ResultActivity
import com.syject.eqally.ui.select.emotion.SelectEmotionActivity
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.activity_splash.*


class ActivityMain : BaseActivity(), Runnable, View.OnClickListener {
    private lateinit var screenAdapter: ScreensAdapter
    private val handler = Handler()

    override fun getLayoutID() = R.layout.activity_splash

    override fun afterViewCreated() {
        prepareStatusBar()
        addScreensMenu()
        moreButton.setOnClickListener(this)
        handler.postDelayed(this, 2000)
    }

    private fun addScreensMenu() {
        screenAdapter = ScreensAdapter(this, ScreensEnums.values().asList()) { screensEnums ->
            showNextScreen(screensEnums)
        }
        screenNavigation.adapter = screenAdapter
    }

    private fun showNextScreen(screensEnums: ScreensEnums) {
        when (screensEnums) {
            ScreensEnums.EMOTIONS -> {
                val intent = Intent(this, SelectEmotionActivity::class.java)
                intent.putExtra(Constants.SELECTED_SCREEN, screensEnums.name)
                showNextActivity(intent)
            }
            ScreensEnums.TAKE_TEST -> {
                showNextActivity(Intent(this, ActivityTakeTest::class.java))
            }
            ScreensEnums.RESULTS -> {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(Constants.IS_FROM_MAIN_SCREEN, true)
                showNextActivity(intent)
            }
        }
    }

    private fun prepareStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, moreButton)
    }

    override fun beforeViewCreated() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun run() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        screensLayout.visibility = View.VISIBLE
        moreButton.visibility = View.VISIBLE
        moreButton
            .animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
        screensLayout
            .animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
    }

    override fun onClick(v: View) {
        showNextActivity(Intent(this, ActivityMore::class.java))
    }
}
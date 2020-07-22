package com.syject.eqally.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.syject.eqally.R
import com.syject.eqally.data.DataManager

abstract class BaseActivity : AppCompatActivity(), BaseView {
    protected val dataManager = DataManager.instance

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeViewCreated()
        setContentView(getLayoutID())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        findViewById<View>(R.id.backButton)?.let { it.setOnClickListener { onBackPressed() } }
        afterViewCreated()
    }

    override fun beforeViewCreated() {

    }

    fun showNextActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun setTitle(titleID: Int) {

    }
}
package com.syject.eqally.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.syject.eqally.R

class FirstTestDialog constructor(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_first_test, null)
        )
        window!!.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp: WindowManager.LayoutParams = window!!.attributes
        lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        lp.dimAmount = 0f
        window!!.attributes = lp
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        findViewById<View>(R.id.closeDialogButton).setOnClickListener { hide() }
        findViewById<View>(R.id.okButton).setOnClickListener { hide() }
    }
}
package com.syject.eqally.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.syject.eqally.R
import com.syject.eqally.data.enums.AchievementEnum
import com.wajahatkarim3.easyflipview.EasyFlipView

class AchievementDescriptionDialog constructor(
    context: Context,
    private val achievement: AchievementEnum
) :
    Dialog(context) {
    private var flipView: EasyFlipView

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_achievements_description, null)
        )
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val lp: WindowManager.LayoutParams = window!!.attributes
        lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        lp.dimAmount = 0f
        window!!.attributes = lp
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        findViewById<View>(R.id.closeDialogButton).setOnClickListener { hide() }
        flipView = findViewById(R.id.flipView)
    }

    override fun show() {
        findViewById<ImageView>(R.id.achievementImage).setImageResource(achievement.imageID)
        findViewById<TextView>(R.id.achievementName).setText(achievement.achievementName)
        findViewById<TextView>(R.id.achievementDescription).setText(achievement.achievementDescription)
        super.show()
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        flipView.flipTheView()
    }

    override fun hide() {
        flipView.setFlipTypeFromRight()
        flipView.setOnFlipListener { _, newCurrentSide ->
            if (newCurrentSide == EasyFlipView.FlipState.FRONT_SIDE) {
                super.hide()
            }
        }
        flipView.flipTheView()
    }
}
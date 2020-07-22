package com.syject.eqally.ui.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import com.syject.eqally.R
import com.syject.eqally.adapters.viewpages.AchievementImageDialogAdapter
import com.syject.eqally.adapters.viewpages.AchievementNameDialogAdapter
import com.syject.eqally.data.models.AchievementInfo
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class AchievementDialog constructor(context: Context) :
    Dialog(context),
    ViewPager.OnPageChangeListener {
    private var achievementViewPager: ViewPager
    private var achievementNameViewPager: ViewPager
    private var pageIndicatorView: PageIndicatorView
    private lateinit var achievementImageDialogAdapter: AchievementImageDialogAdapter

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(
            LayoutInflater.from(context).inflate(R.layout.dialog_achievements, null)
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
        achievementViewPager = findViewById(R.id.achievementImagesViewPager)
        achievementNameViewPager = findViewById(R.id.achievementNameViewPager)
        pageIndicatorView = findViewById(R.id.pageIndicatorView)
    }

    fun showAchievement(list: List<AchievementInfo>) {
        if (list.isEmpty()) return
        if (list.size == 1) {
            pageIndicatorView.visibility = View.INVISIBLE
        }
        pageIndicatorView.count = list.size
        achievementImageDialogAdapter = AchievementImageDialogAdapter(context, list)
        achievementViewPager.adapter = achievementImageDialogAdapter
        achievementNameViewPager.adapter = AchievementNameDialogAdapter(context, list)
        achievementNameViewPager.addOnPageChangeListener(
            OnPageChangeListenerSync(achievementNameViewPager, achievementViewPager)
        )
        achievementViewPager.addOnPageChangeListener(this)
        achievementViewPager.addOnPageChangeListener(
            OnPageChangeListenerSync(achievementViewPager, achievementNameViewPager)
        )
        showAchievementInfo(0)
        show()
    }

    override fun show() {
        super.show()
        val confetti = findViewById<KonfettiView>(R.id.viewConfetti)
        confetti.post {
            confetti.build()
                .addColors(Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 8f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Circle)
                .addSizes(Size(5))
                .setPosition(confetti.x + confetti.width / 2, confetti.y + confetti.height / 3)
                .burst(800)
            Handler().postDelayed({
                findViewById<View>(R.id.backgroundView)
                    .animate()
                    .alpha(0.6f)
                    .duration = 1500
            }, 1500)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }


    override fun onPageSelected(position: Int) {
        showAchievementInfo(position)
    }

    @SuppressLint("SetTextI18n")
    private fun showAchievementInfo(position: Int) {
        val achievementInfo = achievementImageDialogAdapter.getAchievementInfoByPosition(position)
        findViewById<TextView>(R.id.achievementTimes).text =
            "${achievementInfo.countTakes / achievementInfo.achievementEnum.achievementRowCount} " +
                    context.resources.getString(R.string.Achievement_time_s)
        pageIndicatorView.setSelected(position)
    }
}
package com.syject.eqally.ui.app.test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syject.eqally.R
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.QuestionModel
import com.syject.eqally.data.models.TestResultModel
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.custom.FirstTestDialog
import com.syject.eqally.ui.custom.TestBottomButtonView
import com.syject.eqally.ui.result.ResultActivity
import com.syject.eqally.utils.AnimationUtils
import com.syject.eqally.utils.AppUtils
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.custom_test_bottom_panel.*
import java.util.*


class ActivityTest : BaseActivity(), View.OnClickListener {
    private lateinit var selectedTest: TestEnums
    private lateinit var dialogAnswers: BottomSheetDialog
    private lateinit var exitDialog: AlertDialog
    private lateinit var testResultModel: TestResultModel
    private lateinit var questionModel: QuestionModel
    private lateinit var questionProvider: QuestionProvider

    private var testIndex = 0
    private var isRepeatPressed = false
    private var repeatPressCount = 0
    override fun getLayoutID() = R.layout.activity_test

    override fun afterViewCreated() {
        selectedTest = intent.extras!!.getSerializable(Constants.SELECTED_TEST) as TestEnums
        testResultModel = TestResultModel(selectedTest)
        startTest()
        readyButton.setOnClickListener(this)
        repeatButton.setOnClickListener(this)
        answerButton.setOnClickListener(this)
        createBottomDialog()
        createExitDialog()
        if (dataManager.isFirstOpenTest()) {
            Handler().postDelayed({
                FirstTestDialog(this).show()
            }, 500)
        }
    }

    override fun beforeViewCreated() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun createExitDialog() {
        exitDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.Dialog_exit_test_title))
            .setMessage(getString(R.string.Dialog_exit_test_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                super.onBackPressed()
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .create()
        exitDialog.setOnShowListener {
            exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.darkGreyBlue))
            exitDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
        }
    }

    override fun onBackPressed() {
        exitDialog.show()
    }

    private fun handleAnswer(emotion: EmotionEnums) {
        hideDialog()
        neutralImage.setImageResource(android.R.color.transparent)
        emotionImage.setImageResource(android.R.color.transparent)
        updateTestResult(emotion)
        if (testIndex == testResultModel.testCount) handleResult()
        else updateTest()
    }

    private fun handleResult() {
        dataManager.checkTestAchievement(testResultModel)
        dataManager.saveLastTestResult(testResultModel)
        showNextScreen()
    }

    private fun showNextScreen() {
        if (!hasPaidTest() && selectedTest == TestEnums.DEMO) {
            val notPaidTestList = getNotPaidTests()
            val intent = Intent(this, ActivityAdbTest::class.java)
            intent.putExtra(
                Constants.CURRENT_TEST,
                if (notPaidTestList.size == 1) notPaidTestList[0]
                else notPaidTestList[(notPaidTestList.indices).random()]
            )
            showNextActivity(intent)
            finish()
        } else {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(Constants.IS_FROM_MAIN_SCREEN, false)
            showNextActivity(intent)
            finish()
        }
    }

    private fun updateTestResult(emotion: EmotionEnums) {
        testResultModel.addAnswer(
            EmotionEnums.getEmotionByID(questionModel.emotionID),
            emotion.id == questionModel.emotionID,
            repeatPressCount > 0
        )
    }

    @SuppressLint("InflateParams")
    private fun createBottomDialog() {
        dialogAnswers = BottomSheetDialog(this)
        dialogAnswers.setCancelable(false)
        dialogAnswers.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        dialogAnswers.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val dialogView = layoutInflater.inflate(R.layout.dialog_test_bottom_sheet, null)
        setDialogListener(
            dialogView,
            intArrayOf(
                R.id.angerButton,
                R.id.disgustButton,
                R.id.contemptButton,
                R.id.fearButton,
                R.id.sadnessButton,
                R.id.surpriseButton,
                R.id.happinessButton
            )
        )
        dialogAnswers.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val layout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet.height
            layout.parent.requestLayout()
        }
        dialogAnswers.setContentView(dialogView)
    }

    private fun startTest() {
        testIndex = 0
        bottomPanel.changeBottomPanelState(TestBottomButtonView.TestState.NEW_TEST)
        val allTestFiles =
            AppUtils.groupEmotionsFiles(resources.assets.list(selectedTest.testPackageName)!!)
        questionProvider = QuestionProvider(AppUtils.applyTestLimit(selectedTest, allTestFiles))
        updateTest()
    }


    private fun setDialogListener(view: View, ids: IntArray) {
        ids.forEach {
            view.findViewById<Button>(it).setOnClickListener(this)
        }
    }

    private fun hideDialog() {
        if (dialogAnswers.isShowing) dialogAnswers.hide()
    }

    private fun updateTest() {
        repeatPressCount = 0
        bottomPanel.changeBottomPanelState(TestBottomButtonView.TestState.READY)
        questionModel = questionProvider.getQuestion()
        Log.d(
            "12345", getString(EmotionEnums.getEmotionByID(questionModel.emotionID).emotionName))
                    updateTestProgress ()
                    updateImages ()
    }

    private fun updateImages() {
        decodeImage(questionModel.emotionImage, object : AppUtils.DecodeListener {
            override fun onDecode(bitmap: Bitmap) {
                emotionImage.setImageBitmap(bitmap)
            }
        })
        decodeImage(questionModel.neutralImage, object : AppUtils.DecodeListener {
            override fun onDecode(bitmap: Bitmap) {
                neutralImage.setImageBitmap(bitmap)
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun updateTestProgress() {
        countProgress.text = "${++testIndex} / ${testResultModel.testCount}"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.answerButton -> {
                dialogAnswers.show()
            }
            R.id.repeatButton -> {
                isRepeatPressed = true
                repeatPressCount++
                testResultModel.increaseRepeat()
                showEmotions()
                if (repeatPressCount == 5) {
                    bottomPanel.isEnabled = false
                }
            }
            R.id.readyButton -> {
                bottomPanel.changeBottomPanelState(TestBottomButtonView.TestState.SHOWING_EMOTION)
                showEmotions()
            }
            R.id.angerButton -> {
                handleAnswer(EmotionEnums.ANGER)
            }
            R.id.disgustButton -> {
                handleAnswer(EmotionEnums.DISGUST)
            }
            R.id.contemptButton -> {
                handleAnswer(EmotionEnums.CONTEMPT)
            }
            R.id.fearButton -> {
                handleAnswer(EmotionEnums.FEAR)
            }
            R.id.sadnessButton -> {
                handleAnswer(EmotionEnums.SADNESS)
            }
            R.id.surpriseButton -> {
                handleAnswer(EmotionEnums.SURPRISE)
            }
            R.id.happinessButton -> {
                handleAnswer(EmotionEnums.HAPPINESS)
            }
        }
    }

    private fun showEmotions() {
        bottomPanel.changeBottomPanelState(TestBottomButtonView.TestState.SHOWING_EMOTION)
        Handler().postDelayed({
            AnimationUtils.alphaAnimation(emotionImage, null, 1f)
            Handler().postDelayed({ hideEmotion() }, 400)
        }, 1000)
    }

    private fun hideEmotion() {
        emotionImage.clearAnimation()
        AnimationUtils.alphaAnimation(emotionImage, object : AnimationUtils.OnAnimationEndListener {
            override fun onAnimationEnd() {
                bottomPanel.changeBottomPanelState(TestBottomButtonView.TestState.ANSWER_OR_REPEAT)
            }
        }, 0f)
    }

    private fun decodeImage(imageFile: String, listener: AppUtils.DecodeListener) {
        AppUtils.decodeImages(
            this,
            "${selectedTest.testPackageName}/$imageFile",
            listener
        )
    }

    private fun getNotPaidTests() =
        TestEnums.values()
            .filter { testEnums ->
                testEnums != TestEnums.DEMO && !dataManager.isTestPaid(testEnums)
            }

    private fun hasPaidTest(): Boolean {
        return TestEnums
            .values()
            .any { testEnums -> testEnums != TestEnums.DEMO && dataManager.isTestPaid(testEnums) }
    }

    class QuestionProvider constructor(private val testFiles: TreeMap<Int, MutableList<String>>) {

        fun getQuestion(): QuestionModel {
            val emotionID = getRandomEmotionId()
            val emotionImage = getRandomEmotionByEmotionId(emotionID)
            val neutralImage = getNeutralImageFromEmotionImage(emotionImage)
            return QuestionModel(neutralImage, emotionImage, emotionID)
        }

        private fun getRandomEmotionByEmotionId(emotionID: Int): String {
            val emotionFile =
                testFiles[emotionID]!!.removeAt((0 until testFiles[emotionID]!!.size).random())
            if (testFiles[emotionID]!!.size == 0) testFiles.remove(emotionID)
            return emotionFile
        }

        private fun getRandomEmotionId() =
            testFiles.keys.toIntArray()[(1 until testFiles.keys.size).random()]

        private fun getNeutralImageFromEmotionImage(emotionFile: String): String {
            val faceID = AppUtils.getNumberByPosition(emotionFile, 0)
            val faceGroup = AppUtils.getNumberByPosition(emotionFile, 2)
            testFiles[0]!!.forEach {
                if (AppUtils.getNumberByPosition(it, 0) == faceID &&
                    AppUtils.getNumberByPosition(it, 2) == faceGroup
                ) return it
            }
            return ""
        }

    }
}
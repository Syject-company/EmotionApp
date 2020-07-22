package com.syject.eqally.data.models

import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.enums.TestEnums

class TestResultModel constructor(val test: TestEnums) {
    var testCount: Int = 0
    var replaysCount = 0
    var currentRightsAnswerWithOutRepeat = 0
    var currentRightsAnswer = 0
    val resultMap = HashMap<String, CurrentEmotionResult>()

    init {
        testCount = test.questionCount
    }

    fun addAnswer(emotionEnums: EmotionEnums, isRightAnswer: Boolean, isRepeat: Boolean) {
        createEmotionResult(emotionEnums)
        val emotionResult = resultMap[emotionEnums.name]!!
        emotionResult.countQuestions++
        if (isRightAnswer) {
            currentRightsAnswer++
            if (!isRepeat) currentRightsAnswerWithOutRepeat++
            emotionResult.countRightsAnswer++
        }
    }

    fun getRealScope() = "${currentRightsAnswerWithOutRepeat * 100 / testCount}%"

    fun getCorrectAnswerString() = "$currentRightsAnswer / $testCount"

    private fun createEmotionResult(emotionEnums: EmotionEnums) {
        if (!resultMap.containsKey(emotionEnums.name)) {
            resultMap[emotionEnums.name] = CurrentEmotionResult()
        }
    }

    fun increaseRepeat() {
        replaysCount++
    }

    inner class CurrentEmotionResult {
        var countRightsAnswer = 0
        var countQuestions = 0
    }
}
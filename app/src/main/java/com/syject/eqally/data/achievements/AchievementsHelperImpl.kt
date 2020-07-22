package com.syject.eqally.data.achievements

import android.content.Context
import com.syject.eqally.data.enums.AchievementEnum
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.AchievementInfo
import com.syject.eqally.data.models.TestResultModel
import com.syject.eqally.utils.GSONUtils

class AchievementsHelperImpl constructor(val context: Context) : AchievementsHelper {
    private var achievements = HashMap<String, AchievementInfo>()
    private var learningAchievementStates = HashMap<String, MutableList<Boolean>>()

    override fun initAchievement(achievementJSON: String?) {
        achievementJSON?.let {
            achievements = GSONUtils.fromJsonArrayToMap(it)
        }
    }

    override fun initLearnState(learningStateJSON: String?) {
        if (learningStateJSON == null) createEmptyLearningAchievementStates()
        else learningAchievementStates = GSONUtils.fromJsonArrayToMap(learningStateJSON)
    }

    private fun createEmptyLearningAchievementStates() {
        EmotionEnums.values().toMutableList().apply { removeAt(0) }
            .forEach { emotion ->
                learningAchievementStates[emotion.name] =
                    MutableList(context.resources.getStringArray(emotion.emotionCards).size) { false }
            }
    }

    private fun addAchievement(achievementEnum: AchievementEnum) {
        val achievementInfo =
            if (achievements.containsKey(achievementEnum.name)) achievements[achievementEnum.name]!!
            else AchievementInfo(achievementEnum)
        achievementInfo.countTakes++
        achievementInfo.isNew =
            (achievementInfo.countTakes >= achievementEnum.achievementRowCount) &&
                    (achievementInfo.countTakes % achievementEnum.achievementRowCount == 0)
        if (achievementInfo.isNew) {
            achievementInfo.achievementTimeStamp = System.currentTimeMillis()
        }
        achievements[achievementEnum.name] = achievementInfo
    }

    override fun getAchievementInfo(achievementEnum: AchievementEnum) =
        achievements[achievementEnum.name]

    override fun getNewAchievement(): List<AchievementInfo> {
        val tempList = mutableListOf<AchievementInfo>()
        achievements.forEach { (_, achievementInfo) ->
            if (achievementInfo.isNew) {
                achievementInfo.isNew = false
                tempList.add(achievementInfo)
            }
        }
        return tempList
    }

    override fun getAchievementData(): HashMap<String, AchievementInfo> = achievements

    private fun removeAchievementImprovement() {
        getAchievementInfo(AchievementEnum.IMPROVEMENT)?.let {
            if (it.countTakes >= AchievementEnum.IMPROVEMENT.achievementRowCount) {
                it.countTakes = it.countTakes - it.countTakes % AchievementEnum.IMPROVEMENT.achievementRowCount
            } else  {
                achievements.remove(AchievementEnum.IMPROVEMENT.name)
            }
        }
    }

    override fun getLearningStatesData(): HashMap<String, MutableList<Boolean>> =
        learningAchievementStates

    override fun resetData() {
        achievements = HashMap()
        learningAchievementStates.clear()
        createEmptyLearningAchievementStates()
    }

    override fun checkTestAchievement(currentTest: TestResultModel, lastResult: TestResultModel?) {
        if (isGetAchievement(AchievementEnum.CURIOSITY, null, null))
            addAchievement(AchievementEnum.CURIOSITY)
        if (currentTest.test == TestEnums.DEMO) return
        if (isGetAchievement(AchievementEnum.SUPER_RECOGNIZER, currentTest, null)) {
            addAchievement(AchievementEnum.SUPER_RECOGNIZER)
        }
        if (lastResult != null && lastResult.test != TestEnums.DEMO) {
            if (isGetAchievement(AchievementEnum.IMPROVEMENT, currentTest, lastResult))
                addAchievement(AchievementEnum.IMPROVEMENT)
            else removeAchievementImprovement()
        }
        if (isGetAchievement(AchievementEnum.MASTER_RECOGNIZER, currentTest, null)) addAchievement(
            AchievementEnum.MASTER_RECOGNIZER
        )
        currentTest.resultMap.forEach { (emotionName, emotionResult) ->
            if (emotionResult.countRightsAnswer == emotionResult.countQuestions) {
                addAchievement(EmotionEnums.valueOf(emotionName).emotionAchievementEnum!!)
            }
        }
    }

    private fun isGetAchievement(
        achievement: AchievementEnum,
        currentTest: TestResultModel?,
        lastResult: TestResultModel?
    ): Boolean {
        when (achievement) {
            AchievementEnum.CURIOSITY -> {
                return getAchievementInfo(AchievementEnum.CURIOSITY) == null
            }
            AchievementEnum.IMPROVEMENT -> {
                return lastResult!!.currentRightsAnswerWithOutRepeat < currentTest!!.currentRightsAnswerWithOutRepeat
            }
            AchievementEnum.SUPER_RECOGNIZER -> {
                return currentTest!!.currentRightsAnswerWithOutRepeat == currentTest.testCount
            }
            AchievementEnum.MASTER_RECOGNIZER -> {
                return currentTest!!.replaysCount > 0 && currentTest.currentRightsAnswer == currentTest.testCount
            }
            else -> {
                return false
            }
        }
    }

    override fun checkLearnAchievement(emotion: EmotionEnums, cardIndex: Int): Boolean {
        if (getAchievementInfo(AchievementEnum.KNOWLEDGE) != null) return false
        if (!learningAchievementStates[emotion.name]!![cardIndex])
            learningAchievementStates[emotion.name]!![cardIndex] = true
        learningAchievementStates.forEach { (_, isLearningList) ->
            isLearningList.forEach { if (!it) return false }
        }
        addAchievement(AchievementEnum.KNOWLEDGE)
        return true
    }
}
package com.syject.eqally.data.achievements

import com.syject.eqally.data.enums.AchievementEnum
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.models.AchievementInfo
import com.syject.eqally.data.models.TestResultModel

interface AchievementsHelper {

    fun initAchievement(achievementJSON: String?)

    fun initLearnState(learningStateJSON: String?)

    fun getAchievementInfo(achievementEnum: AchievementEnum): AchievementInfo?

    fun getNewAchievement(): List<AchievementInfo>

    fun getAchievementData(): HashMap<String, AchievementInfo>

    fun getLearningStatesData(): HashMap<String, MutableList<Boolean>>

    fun checkTestAchievement(currentTest: TestResultModel, lastResult: TestResultModel?)

    fun checkLearnAchievement(emotion: EmotionEnums, cardIndex: Int): Boolean

    fun resetData()
}
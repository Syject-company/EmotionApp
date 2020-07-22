package com.syject.eqally.data

import com.syject.eqally.data.achievements.AchievementsHelper
import com.syject.eqally.data.enums.AchievementEnum
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.AchievementInfo
import com.syject.eqally.data.models.RankingTestResultModel
import com.syject.eqally.data.models.TestResultModel
import com.syject.eqally.data.network.NetworkHelper
import com.syject.eqally.data.prefs.PrefsHelper
import com.syject.eqally.utils.GSONUtils

class DataManager {
    private lateinit var prefsHelper: PrefsHelper
    private lateinit var achievementsHelper: AchievementsHelper
    private lateinit var networkHelper: NetworkHelper

    companion object {
        val instance = DataManager()
    }

    fun initDataManager(
        prefsHelper: PrefsHelper,
        achievementsHelper: AchievementsHelper,
        networkHelper: NetworkHelper
    ) {
        this.prefsHelper = prefsHelper
        this.achievementsHelper = achievementsHelper
        this.networkHelper = networkHelper
        this.achievementsHelper.initAchievement(prefsHelper.getAchievement())
        this.achievementsHelper.initLearnState(prefsHelper.getLearnState())
    }

    fun saveLastTestResult(testResultModel: TestResultModel) {
        prefsHelper.saveLastTestResult(GSONUtils.objectToJsonString(testResultModel))
        if (testResultModel.test != TestEnums.DEMO) {
            prefsHelper.saveTestToRanking(testResultModel)
        }
    }

    fun getLastTestResultModel() =
        GSONUtils.fromJsonToObject<TestResultModel>(prefsHelper.getLastTestResult())

    fun isTestPaid(currentTest: TestEnums) = prefsHelper.isPaidTest(currentTest)

    fun getAchievementInfo(achievementEnum: AchievementEnum) =
        achievementsHelper.getAchievementInfo(achievementEnum)

    fun getNewAchievement(): List<AchievementInfo> {
        val newAchievements = achievementsHelper.getNewAchievement()
        prefsHelper.saveAchievement(GSONUtils.objectToJsonString(achievementsHelper.getAchievementData()))
        return newAchievements
    }

    fun registerUser(userName: String) =
        networkHelper.registerUser(userName, prefsHelper.getUserID())

    fun getRanking() =
        if (!prefsHelper.isUserRegister()) networkHelper.getGlobalRanking(prefsHelper.getUserID())
        else networkHelper.getUserRanking(prefsHelper.getUserID())

    fun sendUserLastsTests(lastResults: List<RankingTestResultModel>) =
        networkHelper.sendUserLastTest(prefsHelper.getUserID(), lastResults)

    fun savePaidTest(testEnums: TestEnums) {
        prefsHelper.savePaidTest(testEnums)
    }

    fun setUserIsRegister() {
        prefsHelper.setUserIsRegister()
    }

    fun isUserRegister() =
        prefsHelper.isUserRegister()

    fun getRankingTests() =
        GSONUtils.fromJsonArrayToObjectList<RankingTestResultModel>(prefsHelper.getTestToRanking())

    fun isFirstOpenTest() = prefsHelper.isFirstOpenTest()

    fun resetData() {
        prefsHelper.resetData()
        achievementsHelper.resetData()
    }

    fun checkTestAchievement(testResultModel: TestResultModel) {
        achievementsHelper.checkTestAchievement(testResultModel, getLastTestResultModel())
        prefsHelper.saveAchievement(GSONUtils.objectToJsonString(achievementsHelper.getAchievementData()))
    }

    fun checkLearnAchievement(emotion: EmotionEnums, cardIndex: Int): Boolean {
        val isGetLearnAchievement = achievementsHelper.checkLearnAchievement(emotion, cardIndex)
        prefsHelper.saveLearnState(GSONUtils.objectToJsonString(achievementsHelper.getLearningStatesData()))
        if (isGetLearnAchievement)
            prefsHelper.saveAchievement(GSONUtils.objectToJsonString(achievementsHelper.getAchievementData()))
        return isGetLearnAchievement
    }
}
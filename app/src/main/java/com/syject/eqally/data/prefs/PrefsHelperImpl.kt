package com.syject.eqally.data.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.RankingTestResultModel
import com.syject.eqally.data.models.TestResultModel
import com.syject.eqally.utils.GSONUtils
import java.text.SimpleDateFormat
import java.util.*

class PrefsHelperImpl constructor(private val sharedPreferences: SharedPreferences) : PrefsHelper {
    private val lastResultKey = "last_result"
    private val achievementKey = "achievement"
    private val learningStateKey = "learning_states"
    private val userIDKey = "user_id"
    private val isFirstOpenTestKey = "is_first_open_test"
    private val userIsRegisterKey = "user_is_register"
    private val lastResultsForRanking = "last_results_for_ranking"

    override fun saveLastTestResult(result: String) {
        getEditor().putString(lastResultKey, result).apply()
    }

    override fun getLastTestResult(): String? =
        sharedPreferences.getString(lastResultKey, null)

    override fun getAchievement(): String? =
        sharedPreferences.getString(achievementKey, null)

    override fun saveAchievement(achievementJSON: String) {
        getEditor().putString(achievementKey, achievementJSON).apply()
    }

    override fun getLearnState() =
        sharedPreferences.getString(learningStateKey, null)

    override fun saveLearnState(learningStateJSON: String) {
        getEditor().putString(learningStateKey, learningStateJSON).apply()
    }

    override fun getUserID() =
        if (sharedPreferences.contains(userIDKey)) sharedPreferences.getString(userIDKey, null)!!
        else {
            val userID = UUID.randomUUID().toString()
            getEditor().putString(userIDKey, userID).apply()
            userID
        }

    override fun savePaidTest(testEnums: TestEnums) {
        getEditor().putBoolean(testEnums.name, true).apply()
    }

    override fun isPaidTest(testEnums: TestEnums) =
        sharedPreferences.getBoolean(testEnums.name, false)

    @SuppressLint("SimpleDateFormat")
    override fun saveTestToRanking(testResultModel: TestResultModel) {
        val requestRankingTestResult =
            RankingTestResultModel(
                testResultModel.currentRightsAnswer,
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().time),
                testResultModel.replaysCount,
                testResultModel.test.testPackageName
            )
        val lastResultModelList: MutableList<RankingTestResultModel> =
            GSONUtils.fromJsonArrayToObjectList(getTestToRanking())
        lastResultModelList.add(requestRankingTestResult)
        if (lastResultModelList.size > 3) {
            lastResultModelList.removeAt(0)
        }
        getEditor().putString(
            lastResultsForRanking,
            GSONUtils.objectToJsonString(lastResultModelList)
        ).apply()
    }

    override fun getTestToRanking() =
        sharedPreferences.getString(lastResultsForRanking, null)

    override fun setUserIsRegister() {
        getEditor().putBoolean(userIsRegisterKey, true).apply()
    }

    override fun isUserRegister() =
        sharedPreferences.getBoolean(userIsRegisterKey, false)

    override fun isFirstOpenTest(): Boolean {
        val isFirstOpenTest = sharedPreferences.getBoolean(isFirstOpenTestKey, true)
        if (isFirstOpenTest) {
            getEditor().putBoolean(isFirstOpenTestKey, false).apply()
        }
        return isFirstOpenTest
    }

    override fun resetData() {
        getEditor().remove(lastResultKey).apply()
        getEditor().remove(achievementKey).apply()
        getEditor().remove(learningStateKey).apply()
        getEditor().remove(userIDKey).apply()
        getEditor().remove(userIsRegisterKey).apply()
        getEditor().remove(lastResultsForRanking).apply()
    }

    private fun getEditor() = sharedPreferences.edit()

}
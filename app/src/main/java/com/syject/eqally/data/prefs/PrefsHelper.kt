package com.syject.eqally.data.prefs

import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.models.TestResultModel

interface PrefsHelper {

    fun saveLastTestResult(result: String)

    fun getLastTestResult(): String?

    fun getAchievement(): String?

    fun saveAchievement(achievementJSON: String)

    fun getLearnState(): String?

    fun saveLearnState(learningStateJSON: String)

    fun getUserID(): String

    fun savePaidTest(testEnums: TestEnums)

    fun isPaidTest(testEnums: TestEnums): Boolean

    fun saveTestToRanking(testResultModel: TestResultModel)

    fun getTestToRanking(): String?

    fun setUserIsRegister()

    fun isUserRegister(): Boolean

    fun isFirstOpenTest(): Boolean

    fun resetData()
}
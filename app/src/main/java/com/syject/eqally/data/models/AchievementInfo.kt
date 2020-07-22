package com.syject.eqally.data.models

import com.syject.eqally.data.enums.AchievementEnum

class AchievementInfo constructor(val achievementEnum: AchievementEnum) {
    var countTakes: Int = 0
    var isNew = false
    var achievementTimeStamp: Long = 0
}
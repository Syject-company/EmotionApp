package com.syject.eqally.data.enums

import com.syject.eqally.R

enum class ScreensEnums constructor(val imageID: Int, val screenName: Int) {
    EMOTIONS(R.drawable.ic_splash_emotion, R.string.SplashActivity_tab_emotions),
    TAKE_TEST(R.drawable.ic_splash_test, R.string.SplashActivity_tab_test),
    RESULTS(R.drawable.ic_splash_result, R.string.SplashActivity_tab_result)
}
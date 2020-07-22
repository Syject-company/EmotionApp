package com.syject.eqally

import android.app.Application
import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.jacksonandroidnetworking.JacksonParserFactory
import com.syject.eqally.data.DataManager
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.data.achievements.AchievementsHelperImpl
import com.syject.eqally.data.network.NetworkHelperImpl
import com.syject.eqally.data.prefs.PrefsHelperImpl
import com.syject.eqally.utils.BillingUtils

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataManager.instance.initDataManager(
            PrefsHelperImpl(getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)),
            AchievementsHelperImpl(this),
            NetworkHelperImpl()
        )
        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
        BillingUtils.instance.initBillingClient(this, getPayTest())
    }

    private fun getPayTest(): List<String> {
        return TestEnums.values()
            .filter { testEnums -> testEnums.skuID.isNotEmpty() }
            .map { list -> list.skuID }.toList()
    }
}
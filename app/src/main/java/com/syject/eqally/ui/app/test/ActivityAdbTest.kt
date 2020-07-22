package com.syject.eqally.ui.app.test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.android.billingclient.api.SkuDetails
import com.syject.eqally.R
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.ui.result.ResultActivity
import com.syject.eqally.utils.BillingUtils
import com.syject.eqally.utils.Constants
import com.syject.eqally.utils.AppUtils
import kotlinx.android.synthetic.main.activity_test_adb.*

class ActivityAdbTest : BaseActivity(), BillingUtils.PaySubscriber, View.OnClickListener {
    private lateinit var currentTest: TestEnums

    override fun getLayoutID() = R.layout.activity_test_adb

    override fun beforeViewCreated() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun afterViewCreated() {
        currentTest = intent.extras!!.getSerializable(Constants.CURRENT_TEST) as TestEnums
        showAdbTest()
        buyButton.setOnClickListener(this)
        BillingUtils.instance.subscribe(this)
    }

    @SuppressLint("InflateParams")
    private fun showAdbTest() {
        AppUtils.decodeImages(this, getTestImagePath(), object : AppUtils.DecodeListener {
            override fun onDecode(bitmap: Bitmap) {
                testImage.setImageBitmap(bitmap)
            }
        })
        resources.getStringArray(currentTest.descriptionArrayResourceID).forEach {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.item_take_test_test_description, null)
            view.findViewById<TextView>(R.id.description).text = it
            descriptionLayout.addView(view)
        }
    }

    private fun getTestImagePath() =
        "${currentTest.testPackageName}${currentTest.testImagePath}"

    override fun onBackPressed() {
        BillingUtils.instance.unsubscribe(this)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(Constants.IS_FROM_MAIN_SCREEN, false)
        showNextActivity(intent)
        finish()
    }

    override fun onPayPurchase() {
        BillingUtils.instance.unsubscribe(this)
        //called onPayPurchase in ActivityTest - fragmentTestCard
        finish()
    }

    @SuppressLint("SetTextI18n")
    override fun onSkuAvailable(skuDetails: SkuDetails) {
        buyButton.text = getString(R.string.by_for) + " " + skuDetails.price
    }

    @SuppressLint("SetTextI18n")
    override fun onSkuNotAvailable() {
        buyButton.text = "${getString(R.string.by_for)} € ${currentTest.defaultPrice}"
    }

    override fun onRestorePurchase() {
        onPayPurchase()
    }

    override fun getSkuID() = currentTest.skuID


    override fun onClick(v: View) {
        when (v.id) {
            R.id.buyButton -> {
                BillingUtils.instance.launchBilling(currentTest.skuID, this)
            }
        }
    }
}

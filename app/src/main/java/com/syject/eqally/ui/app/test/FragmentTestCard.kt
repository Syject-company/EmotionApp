package com.syject.eqally.ui.app.test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.billingclient.api.SkuDetails
import com.syject.eqally.R
import com.syject.eqally.data.DataManager
import com.syject.eqally.data.enums.TestEnums
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.BillingUtils
import com.syject.eqally.utils.Constants
import com.syject.eqally.utils.AppUtils
import kotlinx.android.synthetic.main.item_take_test_test.*

class FragmentTestCard : BaseFragment(), View.OnClickListener, BillingUtils.PaySubscriber {
    private lateinit var currentTest: TestEnums

    companion object {
        fun getInstance(testEnums: TestEnums) =
            FragmentTestCard().apply {
                arguments = Bundle().apply { putSerializable(Constants.CURRENT_TEST, testEnums) }
            }
    }

    override fun getCardView(): CardView? =
        if (fragmentView == null) null
        else fragmentView as CardView

    override fun getLayoutID() = R.layout.item_take_test_test

    override fun afterViewCreated() {
        currentTest = arguments!!.getSerializable(Constants.CURRENT_TEST) as TestEnums
        BillingUtils.instance.subscribe(this)
        buttonTakeTest.setOnClickListener(this)
        showTestInfo()
    }

    @SuppressLint("InflateParams")
    private fun showTestInfo() {
        testName.setText(currentTest.testNameResourceID)
        testHeader.setText(currentTest.headerTextResourceID)
        resources.getStringArray(currentTest.descriptionArrayResourceID).forEach {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_take_test_test_description, null)
            view.findViewById<TextView>(R.id.description).text = it
            descriptionLayout.addView(view)
        }
        AppUtils.decodeImages(
            baseActivity,
            getTestImagePath(),
            object : AppUtils.DecodeListener {
                override fun onDecode(bitmap: Bitmap) {
                    testImage.setImageBitmap(bitmap)
                }
            })
    }

    private fun getTestImagePath() =
        "${currentTest.testPackageName}${currentTest.testImagePath}"

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonTakeTest -> {
                if (!isTestAvailable()) {
                    BillingUtils.instance.launchBilling(currentTest.skuID, baseActivity)
                } else {
                    showTestActivity()
                }
            }
        }
    }

    private fun isTestAvailable() =
        currentTest == TestEnums.DEMO || DataManager.instance.isTestPaid(currentTest)

    override fun onPayPurchase() {
        setAvailableTest()
        showTestActivity()
    }

    private fun showTestActivity() {
        val intent = Intent(baseActivity, ActivityTest::class.java)
        intent.putExtra(Constants.SELECTED_TEST, currentTest)
        baseActivity.showNextActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onSkuAvailable(skuDetails: SkuDetails) {
        if (!isTestAvailable()) {
            buttonTakeTest.text = getString(R.string.by_for) + " " + skuDetails.price
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSkuNotAvailable() {
        if (!isTestAvailable()) {
            buttonTakeTest.text = "${getString(R.string.by_for)} € ${currentTest.defaultPrice}"
        }
    }

    override fun onRestorePurchase() {
        setAvailableTest()
    }

    override fun getSkuID() = currentTest.skuID

    private fun setAvailableTest() {
        DataManager.instance.savePaidTest(currentTest)
        buttonTakeTest.setText(R.string.TestActivity_start_test)
    }

    override fun onDestroyView() {
        BillingUtils.instance.unsubscribe(this)
        super.onDestroyView()
    }
}
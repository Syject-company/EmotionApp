package com.syject.eqally.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult


class BillingUtils {
    private lateinit var billingClient: BillingClient
    private lateinit var skuIDList: List<String>
    private val subscribers = HashMap<String, MutableList<PaySubscriber>>()
    private val skuDetailsMap = HashMap<String, SkuDetails>()

    companion object {
        val instance = BillingUtils()
    }

    fun initBillingClient(context: Context, skuIDList: List<String>) {
        this.skuIDList = skuIDList
        billingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    subscribers[purchases[0].sku]?.forEach { it.onPayPurchase() }
                }
            }.build()
        setupBilling()
    }

    fun subscribe(paySubscriber: PaySubscriber) {
        val skuId = paySubscriber.getSkuID()
        if (!subscribers.containsKey(skuId)) {
            subscribers[skuId] = mutableListOf()
        }
        subscribers[skuId]!!.add(paySubscriber)
        if (skuDetailsMap.containsKey(skuId))
            skuDetailsMap[skuId]?.let { paySubscriber.onSkuAvailable(it) }
        else paySubscriber.onSkuNotAvailable()
    }

    fun querySkuDetails() {
        val skuDetailsParamsBuilder = SkuDetailsParams.newBuilder()
        skuDetailsParamsBuilder.setSkusList(skuIDList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(skuDetailsParamsBuilder.build()) { responseCode, skuDetailsList ->
            if (responseCode.responseCode == BillingClient.BillingResponseCode.OK) {
                for (skuDetails in skuDetailsList) {
                    skuDetailsMap[skuDetails.sku] = skuDetails
                    subscribers[skuDetails.sku]?.forEach { it.onSkuAvailable(skuDetails) }
                }
            }
        }
    }

    fun launchBilling(skuId: String, activity: AppCompatActivity) {
        val purchasesList = queryPurchases()
        purchasesList?.let {
            for (i in it.indices) {
                if (it[i]!!.sku == skuId) {
                    subscribers[it[i]!!.sku]?.forEach { subscriber -> subscriber.onRestorePurchase() }
                    return
                }
            }
        }
        if (!skuDetailsMap.containsKey(skuId)) {
            setupBilling()
        }
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetailsMap[skuId])
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun setupBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {}

            override fun onBillingSetupFinished(billingResponse: BillingResult) {
                if (billingResponse.responseCode == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails()
                }
            }
        })
    }

    fun restorePurchases() {
        val purchasesList = queryPurchases()
        purchasesList?.let {
            for (i in it.indices) {
                subscribers[it[i]!!.sku]?.forEach { subscriber -> subscriber.onRestorePurchase() }
            }
        }
    }

    private fun queryPurchases(): List<Purchase?>? {
        val purchasesResult: PurchasesResult =
            billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        return purchasesResult.purchasesList
    }

    fun unsubscribe(paySubscriber: PaySubscriber) {
        subscribers[paySubscriber.getSkuID()]?.remove(paySubscriber)
    }

    interface PaySubscriber {
        fun onPayPurchase()

        fun onSkuAvailable(skuDetails: SkuDetails)

        fun onSkuNotAvailable()

        fun onRestorePurchase()

        fun getSkuID(): String
    }
}
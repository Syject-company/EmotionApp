package com.syject.eqally.data.network

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.syject.eqally.data.models.RankingTestResultModel
import com.syject.eqally.data.models.request.UserRankingRequest
import com.syject.eqally.utils.GSONUtils
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class NetworkHelperImpl : NetworkHelper {
    private val baseUrl = "https://immense-oasis-36231.herokuapp.com"

    override fun registerUser(userName: String, userID: String): ANRequest<ANRequest<*>> =
        createHeaderRequest(AndroidNetworking.post("$baseUrl/user"), userID)
            .addJSONObjectBody(JSONObject().apply { put("username", userName) })
            .build()

    override fun getGlobalRanking(userID: String): ANRequest<ANRequest<*>> =
        createHeaderRequest(AndroidNetworking.get("$baseUrl/ranking"), userID)
            .build()

    override fun getUserRanking(userID: String): ANRequest<ANRequest<*>> =
        createHeaderRequest(AndroidNetworking.get("$baseUrl/ranking/${userID}"), userID)
            .build()

    override fun sendUserLastTest(userID: String, lastResults: List<RankingTestResultModel>)
            : ANRequest<ANRequest<*>> =
        createHeaderRequest(AndroidNetworking.post("$baseUrl/results/${userID}"), userID)
            .addJSONObjectBody(
                JSONObject(GSONUtils.objectToJsonString(UserRankingRequest(lastResults)))
            )
            .build()

    private fun createHeaderRequest(request: ANRequest.PostRequestBuilder<*>, userID: String) =
        request
            .addHeaders("Api-Auth", userID.encode())
            .addHeaders("Api-Auth-User", userID)

    private fun createHeaderRequest(request: ANRequest.GetRequestBuilder<*>, userID: String) =
        request
            .addHeaders("Api-Auth", userID.encode())
            .addHeaders("Api-Auth-User", userID)

    private fun String.encode(): String {
        val algorithm = "HmacSHA1"
        val keyString = "aa3116cf-1009-4d8f-9122-69525d0ea649"
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(keyString.toByteArray(Charset.forName("UTF-8")), algorithm))
        val formatter = Formatter()
        for (b in mac.doFinal("$this${this.length}".toByteArray(Charset.forName("UTF-8")))) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }
}
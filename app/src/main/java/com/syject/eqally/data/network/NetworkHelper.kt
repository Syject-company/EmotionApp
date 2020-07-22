package com.syject.eqally.data.network

import com.androidnetworking.common.ANRequest
import com.syject.eqally.data.models.RankingTestResultModel

interface NetworkHelper {

    fun registerUser(userName: String, userID: String): ANRequest<*>

    fun getGlobalRanking(userID: String): ANRequest<*>

    fun getUserRanking(userID: String): ANRequest<*>

    fun sendUserLastTest(userID: String, lastResults: List<RankingTestResultModel>): ANRequest<*>
}
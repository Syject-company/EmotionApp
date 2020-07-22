package com.syject.eqally.data.models

class RankingTestResultModel constructor(
    val correctCount: Int,
    val timestamp: String,
    val repeatCount: Int,
    val bundleId: String
)
package com.syject.eqally.data.enums

import com.syject.eqally.R
import java.io.Serializable

enum class MoreTabsEnums constructor(val tabName: Int, val text: Int) : Serializable {
    THERMS(R.string.MoreActivity_therms, R.string.More_therms_and_condition),
    POLICY(R.string.MoreActivity_privacy_policy, R.string.More_privacy_policy),
    ABOUT(R.string.MoreActivity_about, R.string.More_about)
}
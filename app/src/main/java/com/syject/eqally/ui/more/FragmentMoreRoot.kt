package com.syject.eqally.ui.more

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.syject.eqally.BuildConfig
import com.syject.eqally.R
import com.syject.eqally.adapters.MoreAdapter
import com.syject.eqally.data.enums.MoreTabsEnums
import com.syject.eqally.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_more_root.*

class FragmentMoreRoot : BaseFragment(), View.OnClickListener {
    private lateinit var resetDialog: AlertDialog
    override fun getLayoutID() = R.layout.fragment_more_root

    override fun afterViewCreated() {
        createExitDialog()
        resetDataButton.setOnClickListener(this)
        moreTabs.adapter = MoreAdapter(baseActivity, MoreTabsEnums.values().toList()) {
            (baseActivity as ActivityMore).showMoreTab(it)
        }
        appVersion.append(" ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }


    private fun createExitDialog() {
        resetDialog = AlertDialog.Builder(baseActivity)
            .setTitle(getString(R.string.Dialog_reset_title))
            .setMessage(getString(R.string.Dialog_reset_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dataManager.resetData()
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .create()
        resetDialog.setOnShowListener {
            resetDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(baseActivity, R.color.darkGreyBlue))
            resetDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
        }
    }

    override fun onResume() {
        super.onResume()
        baseActivity.setTitle(R.string.MoreActivity_title)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.resetDataButton -> {
                resetDialog.show()
            }
        }
    }
}
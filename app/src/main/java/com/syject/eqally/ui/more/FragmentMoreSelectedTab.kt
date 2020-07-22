package com.syject.eqally.ui.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.syject.eqally.R
import com.syject.eqally.data.enums.MoreTabsEnums
import com.syject.eqally.ui.base.BaseFragment
import com.syject.eqally.utils.Constants
import kotlinx.android.synthetic.main.fragment_more_description.*


class FragmentMoreSelectedTab private constructor() : BaseFragment() {

    companion object {
        fun getInstance(moreTab: MoreTabsEnums) =
            FragmentMoreSelectedTab().apply {
                arguments = Bundle().apply { putSerializable(Constants.MORE_TAB, moreTab) }
            }
    }

    override fun getLayoutID() = R.layout.fragment_more_description

    @SuppressLint("SetJavaScriptEnabled")
    override fun afterViewCreated() {
        val selectedMoreTab = arguments!!.getSerializable(Constants.MORE_TAB) as MoreTabsEnums
        moreDescriptionWebView.settings.javaScriptEnabled = true
        moreDescriptionWebView.loadDataWithBaseURL(
            null,
            getHTMLData(selectedMoreTab),
            "text/html",
            "UTF-8",
            null
        )
        moreDescriptionWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                moreDescriptionWebView.animate().alpha(1f).duration = 200
            }
        }
        baseActivity.setTitle(selectedMoreTab.tabName)
    }

    private fun getHTMLData(moreTab: MoreTabsEnums) =
        "<html>\n" +
                "<head>\n" +
                "    <style type=\"text/css\">\n" +
                "    body {\n" +
                "      font-size: 17px;\n" +
                "      color: #71718B" +
                "    }\n" +
                "    h1 {\n" +
                "        font-size:24px;\n" +
                "        color: #313A50" +
                "    }\n" +
                "    h2, h3 {\n" +
                "      font-size:20px;\n" +
                "      color: #313A50" +
                "    }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"background-color:#CCCCCC;\">\n" +
                getString(moreTab.text) +
                "</body>\n" +
                "</html>  "
}
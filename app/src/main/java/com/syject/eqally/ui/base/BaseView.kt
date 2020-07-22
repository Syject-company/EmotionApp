package com.syject.eqally.ui.base

interface BaseView {

    fun beforeViewCreated()

    fun getLayoutID(): Int

    fun afterViewCreated()

    fun setTitle(titleID: Int)
}
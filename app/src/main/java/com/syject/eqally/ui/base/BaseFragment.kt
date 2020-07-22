package com.syject.eqally.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.syject.eqally.data.DataManager
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.learn.emotion.FragmentExampleEmotion
import com.syject.eqally.utils.Constants

abstract class BaseFragment : Fragment(), BaseView {
    protected lateinit var baseActivity: BaseActivity
    protected val dataManager = DataManager.instance

    var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentView = inflater.inflate(getLayoutID(), container, false)
        beforeViewCreated()
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        baseActivity = activity as BaseActivity
        afterViewCreated()
    }

    override fun beforeViewCreated() {

    }

    open fun getCardView(): CardView? = null

    override fun setTitle(titleID: Int) {
        baseActivity.setTitle(titleID)
    }
}
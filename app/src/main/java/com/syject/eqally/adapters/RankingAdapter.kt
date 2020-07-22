package com.syject.eqally.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.syject.eqally.R
import com.syject.eqally.data.models.response.ResponseUserPosition
import com.syject.eqally.data.models.response.ResponseUserRanking
import com.syject.eqally.ui.base.BaseActivity

class RankingAdapter(
    baseActivity: BaseActivity,
    onItemClick: (ResponseUserPosition) -> Unit
) : AppBaseAdapter<ResponseUserPosition>(baseActivity, onItemClick) {
    private var userPosition = -1

    override fun isCallClick(item: ResponseUserPosition?, position: Int) = false

    override fun getLayoutId() = R.layout.item_result_ranking

    fun attachRanking(responseUserRanking: ResponseUserRanking) {
        clearItems()
        userPosition = responseUserRanking.userPosition
        addItems(responseUserRanking.positions)
    }

    override fun createHolder(inflate: View) = object : BaseItem<ResponseUserPosition>(inflate) {
        val rankingPlaceTextView = inflate.findViewById<TextView>(R.id.rankingPlace)
        val rankingBackground = inflate.findViewById<View>(R.id.backgroundLayout)
        val userName = inflate.findViewById<TextView>(R.id.userName)

        @SuppressLint("SetTextI18n")
        override fun bind(t: ResponseUserPosition) {
            rankingPlaceTextView.text = "${t.position}"
            userName.text = t.userName
            if (userPosition == t.position) onItemSelected()
            else onItemUnSelected()
        }

        override fun onItemSelected() {
            userName.setTypeface(userName.typeface, Typeface.BOLD)
            rankingPlaceTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.white
                )
            )
            rankingPlaceTextView.setBackgroundResource(R.drawable.background_ranking_circle_user)
            rankingBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.beige))
        }

        override fun onItemUnSelected() {
            userName.setTypeface(userName.typeface, Typeface.NORMAL)
            rankingPlaceTextView.setTextColor(ContextCompat.getColor(context, R.color.blueGreen))
            rankingPlaceTextView.setBackgroundResource(R.drawable.background_ranking_circle)
            rankingBackground.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
        }
    }
}
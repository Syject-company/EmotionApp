package com.syject.eqally.adapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.syject.eqally.R
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.ui.base.BaseActivity
import com.syject.eqally.utils.AppUtils

class EmotionsAdapter(
    baseActivity: BaseActivity,
    list: List<EmotionEnums>,
    onItemClick: (EmotionEnums) -> Unit
) : AppBaseAdapter<EmotionEnums>(baseActivity, list, onItemClick) {

    override fun isCallClick(item: EmotionEnums?, position: Int) = true

    override fun getLayoutId() = R.layout.item_emotion

    override fun createHolder(inflate: View) = object : BaseItem<EmotionEnums>(inflate) {
        val emotionImage = inflate.findViewById<ImageView>(R.id.emotionImage)
        val emotionName = inflate.findViewById<TextView>(R.id.emotionName)

        override fun bind(t: EmotionEnums) {
            emotionName.setText(t.emotionName)
            AppUtils.decodeImages(context, t.emotionFile, object : AppUtils.DecodeListener {
                override fun onDecode(bitmap: Bitmap) {
                    emotionImage.setImageBitmap(bitmap)
                }
            })
        }
    }
}
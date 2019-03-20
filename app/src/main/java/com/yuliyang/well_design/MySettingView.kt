package com.yuliyang.well_design

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.item_setting_view.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.dip

class MySettingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_setting_view, this, true)
        backgroundColor = Color.YELLOW
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        post {
            val layoutParams = itemContent.layoutParams as MarginLayoutParams
            layoutParams.leftMargin = itemTitle.right + dip(8)
            itemContent.layoutParams = layoutParams

            (divideLine.layoutParams as MarginLayoutParams).leftMargin = dip(12)
        }
    }
}
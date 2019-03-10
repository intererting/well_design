package com.yuliyang.well_design.zhihuscroll

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent

class ZhiHuScrollParent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), NestedScrollingParent {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun requestLayout() {
        super.requestLayout()
    }
}
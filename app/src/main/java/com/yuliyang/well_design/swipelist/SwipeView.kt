package com.yuliyang.well_design.swipelist

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.R.layout
import android.widget.LinearLayout
import org.jetbrains.anko.dip


class SwipeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val buttonWidth = dip(100)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(
//                MeasureSpec.getSize(widthMeasureSpec) + buttonWidth,
//                MeasureSpec.getSize(heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val linearLayout = getChildAt(0) as LinearLayout
        val linearLayoutLeft = linearLayout.getChildAt(0)
        val linearLayoutRight = linearLayout.getChildAt(1)
        linearLayout.layout(linearLayout.left, linearLayout.top, linearLayout.left + measuredWidth + buttonWidth, linearLayout.bottom)
        linearLayoutLeft.layout(linearLayoutLeft.left, linearLayoutLeft.top, linearLayoutLeft.left + measuredWidth, linearLayoutLeft.bottom)
        linearLayoutRight.layout(linearLayoutLeft.left + measuredWidth, linearLayoutLeft.top, linearLayoutLeft.left + measuredWidth + buttonWidth, linearLayoutLeft.bottom)

    }

}
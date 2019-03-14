package com.yuliyang.well_design.swipelist

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import org.jetbrains.anko.dip

class SwipeViewB @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val buttonWidth = dip(100)
    private var touchStart: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val swipeViewLeft = getChildAt(0)
        val swipeViewRight = getChildAt(1)

        swipeViewLeft.layout(0, 0, r, measuredHeight)
        swipeViewRight.layout(r, 0, r + buttonWidth, measuredHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart = event.x
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val distance = event.x - touchStart
                scrollBy(distance.toInt(), 0)
            }
        }

        return super.onTouchEvent(event)

    }
}
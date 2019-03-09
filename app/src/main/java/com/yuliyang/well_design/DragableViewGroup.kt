package com.yuliyang.well_design

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout

class DragableViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var totaoTranslateY: Float = 0f

    val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                totaoTranslateY += distanceY
//                println(totaoTranslateY)
//                offsetTopAndBottom((-distanceY).toInt())
                scrollBy(0, distanceY.toInt())
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event);
        return true
    }
}
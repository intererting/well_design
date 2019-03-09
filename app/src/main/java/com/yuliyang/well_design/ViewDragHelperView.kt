package com.yuliyang.well_design

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

class ViewDragHelperView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewDragHelper: ViewDragHelper

    init {
        viewDragHelper = ViewDragHelper.create(
                this,
                1.0f,
                object : ViewDragHelper.Callback() {
                    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                        return true
                    }

                    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                        return top
                    }

                    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                        return left
                    }

                    override fun getViewHorizontalDragRange(child: View): Int {
                        return getMeasuredWidth() - child.getMeasuredWidth();
                    }

                    override fun getViewVerticalDragRange(child: View): Int {
                        return getMeasuredHeight() - child.getMeasuredHeight();
                    }
                })
    }

    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }
}
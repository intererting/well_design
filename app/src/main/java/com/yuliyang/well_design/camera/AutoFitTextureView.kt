package com.yuliyang.well_design.camera

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import android.view.ScaleGestureDetector


class AutoFitTextureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextureView(context, attrs, defStyle), ScaleGestureDetector.OnScaleGestureListener {

    private var ratioWidth = 0
    private var ratioHeight = 0
    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, this)
    var scaleListener: AutoFitTextureViewScaleListener? = null


    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleListener?.onScaleChange(detector.scaleFactor)
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return scaleGestureDetector.onTouchEvent(event)
    }

    fun setAspectRatio(width: Int, height: Int) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        ratioWidth = width
        ratioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (ratioWidth == 0 || ratioHeight == 0) {
            setMeasuredDimension(width, height)
        } else {
            if (width < ((height * ratioWidth) / ratioHeight)) {
                setMeasuredDimension(width, (width * ratioHeight) / ratioWidth)
            } else {
                setMeasuredDimension((height * ratioWidth) / ratioHeight, height)
            }
        }
    }

}


interface AutoFitTextureViewScaleListener {
    fun onScaleChange(ratio: Float)
}


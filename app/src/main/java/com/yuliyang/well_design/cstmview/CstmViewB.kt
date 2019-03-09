package com.yuliyang.well_design.cstmview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CstmViewB @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var perWidth = 0
    private var perHeight = 100f
    private val mPaint = Paint()

    private val pointDatas = arrayListOf<PointInfo>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        perWidth = measuredWidth / 7

        var pointInfo: PointInfo
        for (i in 1..49) {
            val width = mPaint.measureText(i.toString())
            val centerX = perWidth / 2 + ((i - 1) % 7) * perWidth
            val centerY = perHeight / 2 + ((i - 1) / 7) * perHeight
            val x = (perWidth / 2 - width / 2) + ((i - 1) % 7) * perWidth
            val y = perHeight / 2 + Math.abs(mPaint.ascent() + mPaint.descent()) / 2 + ((i - 1) / 7) * perHeight
            pointInfo = PointInfo(i.toString(), centerX.toFloat(), centerY, x, y, i == 13)
            pointDatas.add(pointInfo)
        }
    }

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.textSize = 42f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (pointInfo in pointDatas) {
            if (pointInfo.isToday) {
                canvas.drawText("今", pointInfo.x, pointInfo.y, mPaint)
                canvas.drawCircle(pointInfo.centerX, pointInfo.centerY, 20f, mPaint)
            } else {
                canvas.drawText(pointInfo.text, pointInfo.x, pointInfo.y, mPaint)
            }
        }

    }

    data class PointInfo(var text: String,
                         var centerX: Float,
                         var centerY: Float,
                         var x: Float, var y: Float, var isToday: Boolean)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}
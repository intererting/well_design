package com.yuliyang.well_design.cstmview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CstmViewA @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private val mPathA = Path()
    private val mPathB = Path()
    private val mPathC = Path()
    private val arcWidth = 70
    private var perWidth: Float = 0f
    private val space = 10f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        perWidth = (measuredWidth - space * 2) / 3
        mPathA.moveTo(0f, 0f)
        mPathA.lineTo(0f, measuredHeight.toFloat())
        mPathA.lineTo(perWidth, measuredHeight.toFloat())
        mPathA.lineTo(perWidth + arcWidth, (measuredHeight / 2).toFloat())
        mPathA.lineTo(perWidth, 0f)
        mPathA.close()

        mPathB.moveTo(perWidth + space, 0f)
        mPathB.lineTo(perWidth + arcWidth + space, (measuredHeight / 2).toFloat())
        mPathB.lineTo(perWidth + space, measuredHeight.toFloat())
        mPathB.lineTo(perWidth * 2 + space, measuredHeight.toFloat())
        mPathB.lineTo(perWidth * 2 + arcWidth, (measuredHeight / 2).toFloat())
        mPathB.lineTo(perWidth * 2 + space, 0f)
        mPathB.lineTo(perWidth, 0f)
        mPathB.close()
    }

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED
        mPaint.textSize = 42f
    }

    override fun onDraw(canvas: Canvas) {
        mPaint.color = Color.RED
        canvas.drawPath(mPathA, mPaint)
        mPaint.color = Color.BLUE
        canvas.drawPath(mPathB, mPaint)

        mPaint.color = Color.BLACK

        val baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2
        canvas.drawText("个人信息",
                (measuredWidth - space * 2) / 6 - mPaint.measureText("个人信息") / 2,
                (measuredHeight / 2).toFloat() + baseLineY,
                mPaint)

    }

}
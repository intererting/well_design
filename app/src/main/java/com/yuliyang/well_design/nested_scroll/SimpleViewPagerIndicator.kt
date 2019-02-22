package com.yuliyang.well_design.nested_scroll

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class SimpleViewPagerIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs) {

    private var mTitles: Array<String>? = null
    private var mTabCount: Int = 0
    private var mIndicatorColor = COLOR_INDICATOR_COLOR
    private var mTranslationX: Float = 0F
    private val mPaint = Paint()
    private var mTabWidth: Int = 0

    init {
        mPaint.color = mIndicatorColor
        mPaint.strokeWidth = 9.0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mTabCount > 0) {
            mTabWidth = w / mTabCount
        }
    }

    fun setTitles(titles: Array<String>) {
        mTitles = titles
        mTabCount = titles.size
        generateTitleView()
    }

    fun setIndicatorColor(indicatorColor: Int) {
        this.mIndicatorColor = indicatorColor
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        canvas.translate(mTranslationX, (height - 2).toFloat())
        canvas.drawLine(0f, 0f, mTabWidth.toFloat(), 0f, mPaint)
        canvas.restore()
    }

    fun scroll(position: Int, offset: Float) {
        mTranslationX = width / mTabCount * (position + offset)
        invalidate()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    private fun generateTitleView() {
        removeAllViews()
        if (mTitles.isNullOrEmpty()) {
            return
        }
        val count = mTitles!!.size
        weightSum = count.toFloat()
        for (mTitle in mTitles!!) {
            val tv = TextView(context)
            val lp = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            lp.weight = 1F
            tv.gravity = Gravity.CENTER
            tv.setTextColor(COLOR_TEXT_NORMAL)
            tv.text = mTitle
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            tv.layoutParams = lp
            tv.setOnClickListener { }
            addView(tv)
        }
    }

    companion object {

        private val COLOR_TEXT_NORMAL = -0x1000000
        private val COLOR_INDICATOR_COLOR = Color.BLUE
    }

}

package com.yuliyang.well_design.xformode

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.yuliyang.well_design.R

class TestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val drawable: Drawable = context.resources.getDrawable(R.drawable.test, context.theme)
    private val mMaskPath = Path()
    private val paint = Paint()

    init {
        drawable.setBounds(0, 0, 600, 600)
//        mMaskPath.addCircle(300f, 0f, 100f, Path.Direction.CW)

        mMaskPath.addArc(RectF(0f, 0f, 600f, 600f), 0f, 180f)
//        mMaskPath.fillType = Path.FillType.WINDING
        mMaskPath.fillType = Path.FillType.INVERSE_WINDING
//        paint.color = Color.BLACK
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val savedLayer = canvas.saveLayer(0f, 0f, 600f, 600f, null)
        drawable.draw(canvas)
        canvas.translate(0f, -50f)
        canvas.drawPath(mMaskPath, paint)
//        canvas.drawCircle(600f, 600f, 300f, paint)
        canvas.restoreToCount(savedLayer)
    }
}
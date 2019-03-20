package com.yuliyang.well_design.swipelist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.dip

class MyShapeRecyclerview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var initFlag = true
    private val clipPath = Path()

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (initFlag) {
            println(measuredWidth)
            println(measuredHeight)
            initFlag = false
            clipPath.addRoundRect(
                RectF(
                    0f,
                    0f,
                    measuredWidth.toFloat(),
                    measuredHeight.toFloat()
                ),
                dip(15).toFloat(),
                dip(15).toFloat(),
                Path.Direction.CW
            )
//            clipPath.addCircle(
//                (measuredWidth / 2).toFloat(),
//                (measuredHeight / 2).toFloat(),
//                (measuredWidth / 2).toFloat(),
//                Path.Direction.CW
//            )
        }

    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}
package com.yuliyang.well_design.zhihuscroll

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_zhihu_scroll.*

class ZhiHuScrollActivity : AppCompatActivity() {

    private var startX = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu_scroll)
        container.setOnClickListener {
            container.scrollBy(-100, 0)
        }
        get.setOnClickListener {
            for (i in 0 until container.childCount) {
                container.getChildAt(i).offsetLeftAndRight(container.scrollX - container.getChildAt(i).left)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                container.scrollTo((startX - event.x).toInt(), 0)
                for (i in 0 until container.childCount) {
                    container.getChildAt(i).offsetLeftAndRight(container.scrollX - container.getChildAt(i).left)
                }
            }
        }
        return super.onTouchEvent(event)
    }
}

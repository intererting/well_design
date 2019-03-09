package com.yuliyang.well_design

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_change_posi.*

/**
 * 改变位置测试
 */

class ChangePosiActivity : AppCompatActivity() {

    private var startY: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_posi)
//        move.setOnClickListener {
        //            container.scrollBy(-100, -100)
//            container.offsetTopAndBottom(300)
//            container.animate().translationYBy(300f)
//        }
//        container.setOnClickListener { println("xxx") }
//        moveView.setOnClickListener {
//            //            moveView.translationY = 300f
////            moveView.y=-300f
//            println("click  xxx")
//        }

        container.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
//                    container.y = event.rawY - container.height / 2
                    container.scrollBy(0, (startY - event.y).toInt())
                    startY = event.y
                }
            }
            return@setOnTouchListener true

        }

    }

}
package com.yuliyang.well_design

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_change_posi.*

/**
 * 改变位置测试
 */

class ChangePosiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_posi)
        move.setOnClickListener {
            //            container.scrollBy(-100, -100)
//            container.offsetTopAndBottom(300)
//            container.animate().translationYBy(300f)
        }
        container.setOnClickListener { println("xxx") }
    }
}
package com.yuliyang.well_design

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_glide.*
import java.lang.Exception

class GlideTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)
        val builder = Glide.with(this)
                .downloadOnly()
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1552408464507&di=b040293c10d1dd0e32252f135a06a8db&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f6037cb445933312b31bb151edda.jpg")
        Thread {
            try {
                val result = builder.submit().get()
                println(result.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
//        Glide.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1552408464507&di=b040293c10d1dd0e32252f135a06a8db&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f6037cb445933312b31bb151edda.jpg")
//                .into(test)
    }
}
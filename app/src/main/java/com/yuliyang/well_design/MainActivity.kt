package com.yuliyang.well_design

import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatDrawableManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import org.jetbrains.anko.contentView

class MainActivity : AppCompatActivity() {

    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        loadingButton.addLoaingTextView({
//
//        })

        loadingButton.setOnLoadingListener {

        }

        clickTest.onClickStart {
            println("xxxxxxxxx")
            delay(2000)
        }
        stopLoading.setOnClickListener {
            loadingButton.stopLoading()
        }
    }
}

@ObsoleteCoroutinesApi
fun View.onClickStart(action: suspend () -> Unit) {
    val eventActor = GlobalScope.actor<Unit>(Dispatchers.Main) {
        for (event in channel) {
            action()
        }
    }
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

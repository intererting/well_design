package com.yuliyang.well_design

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class PreLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_loading)

        GlobalScope.launch(Dispatchers.IO) {
            println("开始网络请求")
            val result = GlobalScope.async(Dispatchers.IO) {
                println("网络请求")
                delay(3000)
                println("网络请求结束")
                return@async true
            }
            val netWorkResult = result.await()
            println("netWorkResult  $netWorkResult")
            println("结束网络请求")
        }
    }

//    private fun netWork(): Boolean {
//        GlobalScope.launch(Dispatchers.IO) {
//            println("网络请求")
//            delay(3000)
//            println("网络请求结束")
//        }
//
//    }
}
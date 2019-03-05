package com.yuliyang.well_design

import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

fun main() {

//    val members = Student::class.members
//    members.forEach {
//        if (it.name == "say") {
//            it.call(Student(), "haha")
//        }
//    }

//    RetrofitFactory.createService(TestService::class.java).testBaidu().enqueue(object : Callback<ResponseBody> {
//        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//        }
//
//        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            println(response.code())
//        }
//
//    })

    retrofit {
        method = "testBaidu"
        clazz = TestService::class.java
        params = arrayOf("haha")
    }
}

class Student {
    fun say(name: String) {
        println(name)
    }
}

interface TestService {
    @GET("http://www.baidu.com")
    fun testBaidu(name: String): Call<ResponseBody>
}

class RequestWrapper {

    internal var _success: (String) -> Unit = { }
    internal var _fail: (Throwable) -> Unit = {}

    lateinit var method: String

    lateinit var clazz: Class<*>

    lateinit var params: Array<Any>

    fun onSuccess(onSuccess: (String) -> Unit) {
        _success = onSuccess
    }

    fun onFail(onError: (Throwable) -> Unit) {
        _fail = onError
    }
}

fun retrofit(init: RequestWrapper.() -> Unit) {
    val wrap = RequestWrapper()

    wrap.init()

    executeForResult(wrap)
}

fun executeForResult(wrap: RequestWrapper) {

    val service = RetrofitFactory.createService(wrap.clazz)
    val members = service::class.members
    members.forEach {
        if (it.name == wrap.method) {
            (it.call(service, "") as Call<ResponseBody>).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    println(response.code())
                }

            })
        }
    }
}

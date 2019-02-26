package com.yuliyang.well_design

import android.app.Application
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.properties.Delegates


class MyApplication : Application() {

    companion object {
        private var instance by Delegates.notNull<Application>()

        fun provideInstance(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        val processName = getProcessName(android.os.Process.myPid())
        if (processName == packageName) {
            instance = this
        }
    }

    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}
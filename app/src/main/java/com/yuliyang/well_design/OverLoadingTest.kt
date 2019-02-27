package com.yuliyang.well_design

open class Father {
    open protected fun say(name: String): String {
        return "father"
    }
}

class Son : Father() {
    override fun say(name: String): String {
        return "son"
    }
}
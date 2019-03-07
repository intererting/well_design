package com.yuliyang.well_design.calendar

import java.util.*

fun Calendar.updateStartDateForMonth() {
    set(Calendar.DATE, 1) // 设置成当月第一天
    // 星期一是2 星期天是1 填充剩余天数
    var iDay: Int
    iDay = get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
    if (iDay < 0) {
        iDay = 6
    }
    add(Calendar.DAY_OF_WEEK, -iDay)
    add(Calendar.DAY_OF_MONTH, -1)// 周日第一位
}

fun Calendar.getMonthDays(): List<Date> {
    val list = ArrayList<Date>()
    for (i in 1..42) {
        list.add(time)
        add(Calendar.DAY_OF_MONTH, 1)
    }
    return list
}
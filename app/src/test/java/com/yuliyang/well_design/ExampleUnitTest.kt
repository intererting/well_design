package com.yuliyang.well_design

import com.yuliyang.well_design.calendar.updateStartDateForMonth
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//        val calendar = Calendar.getInstance()
//        println(calendar.get(Calendar.DAY_OF_WEEK))
//        calendar.updateStartDateForMonth()
//        println("${calendar.get(Calendar.MONTH)}  ${calendar.get(Calendar.DAY_OF_MONTH)}")
        val pattern = "yuliyang(?=[12])".toRegex()
        val matches = pattern.findAll("yuliyang1yuliyang1")
        matches.iterator().forEach {
            println(it.value)
        }
    }
}

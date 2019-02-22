package com.yuliyang.well_design.databinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
    private val person = MutableLiveData<Person>()

    fun getPerson(): MutableLiveData<Person> {
        return person
    }
}
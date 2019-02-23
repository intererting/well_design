package com.yuliyang.well_design.databinding

import androidx.lifecycle.MutableLiveData


class Person(accountNum: String = "", password: String = "") {

    private val accountNum = MutableLiveData<String>()
    private val password = MutableLiveData<String>()

    init {
        this.accountNum.postValue(accountNum)
        this.password.postValue(password)
    }

    fun getAccountNum(): MutableLiveData<String> {
        return accountNum
    }

    fun getPassword(): MutableLiveData<String> {
        return password
    }

    override fun toString(): String {
        return "Person(accountNum=${accountNum.value}, password=${password.value})"
    }


}
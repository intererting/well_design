package com.yuliyang.well_design.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_data_binding_lv.*

class DataBindingLVActivity : AppCompatActivity() {

    private val viewmodel by lazy {
        ViewModelProviders.of(this).get(TestViewModel::class.java)
    }

    private lateinit var binding: ActivityDataBindingLvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_lv)
        binding.setLifecycleOwner(this)
        binding.person = viewmodel.getPerson()
        viewmodel.getPerson().postValue(Person())
        getValue.setOnClickListener {
            println(viewmodel.getPerson().value)
        }
    }
}
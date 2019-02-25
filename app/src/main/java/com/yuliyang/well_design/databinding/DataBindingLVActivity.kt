package com.yuliyang.well_design.databinding

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_lv)
        requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        binding.setLifecycleOwner(this)
        viewmodel.getPerson().postValue(Person())
        binding.person = viewmodel.getPerson()
        getValue.setOnClickListener {
            println(viewmodel.getPerson().value)
        }
        setValue.setOnClickListener {
            testTextView.setText("testTextView")
        }

        pickPic.setOnClickListener {
        }
    }
}
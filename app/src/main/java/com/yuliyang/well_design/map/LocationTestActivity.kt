package com.yuliyang.well_design.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yuliyang.well_design.LocationViewModel

class LocationTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        viewModel.locationLivedata.observe(this, Observer {
            println(it.address)
        })
        viewModel.startLocation()
    }
}
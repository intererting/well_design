package com.yuliyang.well_design

import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        start.setOnClickListener {
            TransitionManager.beginDelayedTransition(transactionGroup, Fade());
            targetView.visibility = View.VISIBLE
        }
    }

}
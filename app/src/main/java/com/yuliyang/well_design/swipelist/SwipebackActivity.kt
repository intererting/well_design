package com.yuliyang.well_design.swipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_swipe_back.*

class SwipebackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_back)
        with(listView) {
            layoutManager = LinearLayoutManager(this@SwipebackActivity)
            adapter = SwipebackAdapter()
            addItemDecoration(DividerItemDecoration(this@SwipebackActivity, LinearLayout.VERTICAL))
        }
    }

    class SwipebackAdapter : RecyclerView.Adapter<SwipeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeHolder {
            return SwipeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false))
        }

        override fun getItemCount(): Int {
            return 30
        }

        override fun onBindViewHolder(holder: SwipeHolder, position: Int) {
        }

    }

    class SwipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
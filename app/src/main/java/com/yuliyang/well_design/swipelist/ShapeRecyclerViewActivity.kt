package com.yuliyang.well_design.swipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_shape_recyclerview.*

class ShapeRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shape_recyclerview)
        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(this@ShapeRecyclerViewActivity)
            adapter = ShapeAdapter()
        }
//        mRecyclerView.clipToOutline = true
//        mRecyclerView.outlineProvider = MyArcOutlineProvider()
    }

    class ShapeAdapter : RecyclerView.Adapter<ShapeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShapeHolder {
            return ShapeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false))
        }

        override fun getItemCount(): Int {
            return 30
        }

        override fun onBindViewHolder(holder: ShapeHolder, position: Int) {
        }

    }

    class ShapeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
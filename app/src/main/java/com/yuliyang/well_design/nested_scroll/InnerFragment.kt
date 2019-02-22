package com.yuliyang.well_design.nested_scroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuliyang.well_design.R
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_tab.*
import java.util.*

class InnerFragment : Fragment() {

    private val mDatas = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        for (i in 0..49) {
            mDatas.add("$i")
        }
        mRecyclerView.adapter = object : CommonAdapter<String>(activity, R.layout.item, mDatas) {
            override fun convert(holder: ViewHolder, t: String?, position: Int) {
                holder.setText(R.id.id_info, t)
            }
        }
    }
}
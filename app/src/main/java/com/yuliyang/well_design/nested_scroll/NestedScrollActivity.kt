package com.yuliyang.well_design.nested_scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_nested_scroll.*

class NestedScrollActivity : AppCompatActivity() {

    private val mTitles = arrayOf("简介", "评价", "相关")
    private var mAdapter: FragmentPagerAdapter? = null
    private val mFragments = arrayOfNulls<InnerFragment>(3)
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll)
        mViewPager = findViewById(R.id.id_stickynavlayout_viewpager)
        initEvents()
        initDatas()
    }

    private fun initEvents() {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                indicator.scroll(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun initDatas() {
        indicator.setTitles(mTitles)

        for (i in mTitles.indices) {
            mFragments[i] = InnerFragment()
        }

        mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount() = mTitles.size

            override fun getItem(position: Int): Fragment {
                return mFragments[position]!!
            }
        }
        mViewPager.adapter = mAdapter
    }

}
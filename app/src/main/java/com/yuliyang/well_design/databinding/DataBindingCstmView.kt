package com.yuliyang.well_design.databinding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.cstm_test.view.*

class DataBindingCstmView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: CstmTestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.cstm_test,
            this,
            true
    )
//    private var title: String? = null


    init {
        binding.setLifecycleOwner(context as LifecycleOwner)
//        val cstmAttrs = context.obtainStyledAttributes(attrs, R.styleable.DataBindingCstmView, defStyleAttr, R.style.DataBindingCstm_Defaule)
//        title = cstmAttrs.getString(R.styleable.DataBindingCstmView_itemTitle)
//        val content = cstmAttrs.getString(R.styleable.DataBindingCstmView_itemContent)
//        cstmAttrs.recycle()

        desc.setOnFocusChangeListener { v, hasFocus ->
            println("${v.tag}  ${hasFocus}")
            descInput.error = if (!hasFocus) "输入错误" else ""
        }


    }

    override fun onFinishInflate() {
        super.onFinishInflate()
//        binding.title.text = title
    }

    fun setItemTitles(mContent: String?) {
        title.text = mContent
    }

    fun setContent(content: MutableLiveData<String>?) {
        binding.content = content
    }

//    fun setContent(content: MutableLiveData<Person>) {
//        binding.content = content
//    }
}

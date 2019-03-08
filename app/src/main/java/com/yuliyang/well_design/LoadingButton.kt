package com.yuliyang.well_design

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import org.jetbrains.anko.*

@ObsoleteCoroutinesApi
class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val LOADING_TAG = "loading_tag"
    }

    fun setOnLoadingListener(clickListener: (LoadingButton) -> Unit) {
        if (childCount == 0) {
            throw IllegalStateException("LoadingButton没有包含可点击内容")
        }
        for (i in 0 until childCount) {
            //为每个child设置点击事件
            getChildAt(i).onClickStart {
                relativeLayout {
                    tag = LOADING_TAG
                    isClickable = true
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    backgroundColor = resources.getColor(android.R.color.white)
                    progressBar {
                    }.lparams {
                        width = dip(25)
                        height = dip(25)
                        centerInParent()
                    }
                }
                clickListener.invoke(this)
                delay(200)
            }
        }

    }

    fun stopLoading() {
        if (childCount == 0) {
            throw IllegalStateException("LoadingButton没有包含可点击内容")
        }
        removeView(findViewWithTag(LOADING_TAG))
    }

    /**
     * 代码添加加载框
     */
    fun addLoaingTextView(loadingText: String, clickListener: () -> Unit) {
        textView {
            width = this@LoadingButton.measuredWidth
            height = this@LoadingButton.measuredHeight
            backgroundColor = resources.getColor(R.color.colorAccent)
            text = loadingText
            textColor = resources.getColor(android.R.color.white)
            gravity = Gravity.CENTER
        }.setOnClickListener {
            relativeLayout {
                isClickable = true
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                backgroundColor = resources.getColor(android.R.color.white)
                progressBar {
                }.lparams {
                    width = dip(25)
                    height = dip(25)
                    centerInParent()
                }
            }
            clickListener.invoke()
        }
    }
}
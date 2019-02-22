package com.yuliyang.well_design.nested_scroll

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.yuliyang.well_design.R

/**
 * 带嵌套滑动的ViewGroup
 */
class NestedScrollParentLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), NestedScrollingParent {

    companion object {
        const val TOP_CHILD_FLING_THRESHOLD = 3
    }

    private var mTop: View? = null
    private var mViewPager: ViewPager? = null
    private var mTopViewHeight: Int = 0
    private var mScroller: Scroller
    private var mVelocityTracker: VelocityTracker? = null

    private val mOffsetAnimator by lazy {
        ValueAnimator().apply {
            interpolator = mInterpolator
            addUpdateListener { animation ->
                scrollTo(0, animation.animatedValue as Int)
            }
        }
    }
    private val mInterpolator = AccelerateInterpolator()
    private var mTouchSlop: Int = 0
    private var mMaximumVelocity: Int = 0
    private var mMinimumVelocity: Int = 0
    private var mLastY: Float = 0F
    private var mDragging: Boolean = false

    init {
        orientation = LinearLayout.VERTICAL
        mScroller = Scroller(context)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
    }

    override fun onStopNestedScroll(target: View) {
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val hiddenTop = dy > 0 && scrollY < mTopViewHeight
        val showTop = dy < 0 && scrollY >= 0 && !target.canScrollVertically(-1)
        if (hiddenTop || showTop) {
            mScroller.abortAnimation()
            mOffsetAnimator.cancel()
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        var innerConsumed = consumed
        if (target is RecyclerView && velocityY < 0) {
            val firstChild = target.getChildAt(0)
            val childAdapterPosition = target.getChildAdapterPosition(firstChild)
            innerConsumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
        }
        if (!innerConsumed) {
            animateScroll(velocityY, computeDuration(0f), innerConsumed)
        }
        return true
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (Math.abs(velocityY) > mMinimumVelocity && scrollY < mTopViewHeight) {
            animateScroll(velocityY, computeDuration(velocityY), false)
            return true
        }
        return false
    }

    /**
     * 根据速度计算滚动动画持续时间
     */
    private fun computeDuration(velocityY: Float): Int {
        val distance: Int
        if (velocityY > 0) {
            distance = Math.abs(mTopViewHeight - scrollY)
        } else {
            distance = scrollY
        }
        val duration: Int
        val innerVelocityY = Math.abs(velocityY)
        if (innerVelocityY > 0) {
            duration = Math.round(1000 * (distance / innerVelocityY))
        } else {
            val distanceRatio = distance.toFloat() / height
            duration = (distanceRatio * 400).toInt()
        }
        return duration
    }

    private fun animateScroll(velocityY: Float, duration: Int, consumed: Boolean) {
        mOffsetAnimator.cancel()
        mOffsetAnimator.duration = Math.min(duration, 400).toLong()
        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(scrollY, mTopViewHeight)
            mOffsetAnimator.start()
        } else {
            if (!consumed) {
                mOffsetAnimator.setIntValues(scrollY, 0)
                mOffsetAnimator.start()
            }
        }
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        mVelocityTracker!!.addMovement(event)
        val action = event.action
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished)
                    mScroller.abortAnimation()
                mLastY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mLastY

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true
                }
                if (mDragging) {
                    scrollBy(0, (-dy).toInt())
                }

                mLastY = y
            }
            MotionEvent.ACTION_CANCEL -> {
                mDragging = false
                recycleVelocityTracker()
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_UP -> {
                mDragging = false
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val velocityY = mVelocityTracker!!.yVelocity.toInt()
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY)
                }
                recycleVelocityTracker()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTop = findViewById(R.id.id_stickynavlayout_topview)
        mViewPager = findViewById(R.id.id_stickynavlayout_viewpager) as? ViewPager
                ?: throw RuntimeException(
                "id_stickynavlayout_viewpager show used by ViewPager !")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //不限制顶部的高度
        println("onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        mTopViewHeight = mTop!!.measuredHeight
        setMeasuredDimension(measuredWidth, mTopViewHeight + measuredHeight)
    }

    private fun fling(velocityY: Int) {
        mScroller.fling(0, scrollY, 0, velocityY, 0, 0, 0, mTopViewHeight)
        invalidate()
    }

    override fun scrollTo(x: Int, y: Int) {
        var innerY = y
        if (innerY < 0) {
            innerY = 0
        }
        if (innerY > mTopViewHeight) {
            innerY = mTopViewHeight
        }
        if (innerY != scrollY) {
            super.scrollTo(x, innerY)
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.currY)
            invalidate()
        }
    }
}

package com.flying.dutch.myapplication.view


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs



class SlideRecyclerView @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    RecyclerView(context!!, attrs, defStyle) {
    private var mVelocityTracker: VelocityTracker? = null
    private val mTouchSlop
            : Int
    private var mTouchFrame: Rect? = null
    private val mScroller: Scroller
    private var mLastX = 0f
    private var mFirstX = 0f
    private var mFirstY = 0f
    private var mIsSlide = false
    private var mFlingView: ViewGroup? = null
    private var mPosition = 0
    private var mMenuViewWidth = 0
    private var mMenuViewMarginRight = 0

    init {
        mTouchSlop = ViewConfiguration.get(context!!).scaledTouchSlop
        mScroller = Scroller(context)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val x = e.x.toInt()
        val y = e.y.toInt()
        obtainVelocity(e)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                run {
                    mLastX = x.toFloat()
                    mFirstX = mLastX
                }
                mFirstY = y.toFloat()
                mPosition = pointToPosition(x, y)
                if (mPosition != INVALID_POSITION) {
                    val view: View? = mFlingView
                    mFlingView =
                        getChildAt(mPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()) as ViewGroup
                    if (view != null && mFlingView !== view && view.scrollX != 0) {
                        view.scrollTo(0, 0)
                    }
                    if (mFlingView!!.childCount == 2) {
                        mMenuViewWidth = mFlingView!!.getChildAt(1).width
                        val layoutParams =
                            mFlingView!!.getChildAt(1).layoutParams as MarginLayoutParams
                        mMenuViewMarginRight = layoutParams.marginStart
                    } else {
                        mMenuViewWidth = INVALID_CHILD_WIDTH
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker!!.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker!!.xVelocity
                val yVelocity = mVelocityTracker!!.yVelocity
                if (Math.abs(xVelocity) > SNAP_VELOCITY && Math.abs(xVelocity) > Math.abs(yVelocity)
                    || Math.abs(x - mFirstX) >= mTouchSlop
                    && Math.abs(x - mFirstX) > Math.abs(y - mFirstY)
                ) {
                    mIsSlide = true
                    return true
                }
            }

            MotionEvent.ACTION_UP -> releaseVelocity()
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (mIsSlide && mPosition != INVALID_POSITION) {
            val x = e.x
            obtainVelocity(e)
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE ->
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        val dx = mLastX - x
                        if (mFlingView!!.scrollX + dx <= mMenuViewWidth + mMenuViewMarginRight
                            && mFlingView!!.scrollX + dx > 0
                        ) {
                            mFlingView!!.scrollBy(dx.toInt(), 0)
                        }
                        mLastX = x
                    }

                MotionEvent.ACTION_UP -> {
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        val scrollX = mFlingView!!.scrollX
                        mVelocityTracker!!.computeCurrentVelocity(1000)
                        if (mVelocityTracker!!.xVelocity < -SNAP_VELOCITY) {
                            mScroller.startScroll(
                                scrollX,
                                0,
                                (mMenuViewWidth + mMenuViewMarginRight) - scrollX,
                                0,
                                Math.abs((mMenuViewWidth + mMenuViewMarginRight) - scrollX)
                            )
                        } else if (mVelocityTracker!!.xVelocity >= SNAP_VELOCITY) {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX))
                        } else if (scrollX >= (mMenuViewWidth + mMenuViewMarginRight) / 2) {
                            mScroller.startScroll(
                                scrollX,
                                0,
                                (mMenuViewWidth + mMenuViewMarginRight) - scrollX,
                                0,
                                abs((mMenuViewWidth + mMenuViewMarginRight) - scrollX)
                            )
                        } else {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX))
                        }
                        invalidate()
                    }
                    mMenuViewWidth = INVALID_CHILD_WIDTH
                    mIsSlide = false
                    mPosition = INVALID_POSITION
                    releaseVelocity()
                }
            }
            return true
        } else {
            closeMenu()
            releaseVelocity()
        }
        return super.onTouchEvent(e)
    }

    private fun releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private fun obtainVelocity(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    private fun pointToPosition(x: Int, y: Int): Int {
        val firstPosition = (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        var frame = mTouchFrame
        if (frame == null) {
            mTouchFrame = Rect()
            frame = mTouchFrame
        }
        val count = childCount
        for (i in count - 1 downTo 0) {
            val child = getChildAt(i)
            if (child.visibility == VISIBLE) {
                child.getHitRect(frame)
                if (frame!!.contains(x, y)) {
                    return firstPosition + i
                }
            }
        }
        return INVALID_POSITION
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mFlingView!!.scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    fun closeMenu() {
        if (mFlingView != null && mFlingView!!.scrollX != 0) {
            mFlingView!!.scrollTo(0, 0)
        }
    }

    companion object {
        private const val INVALID_POSITION = -1
        private const val INVALID_CHILD_WIDTH = -1
        private const val SNAP_VELOCITY = 600
    }
}
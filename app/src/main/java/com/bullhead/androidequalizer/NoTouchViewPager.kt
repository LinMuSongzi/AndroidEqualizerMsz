package com.bullhead.androidequalizer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

class NoTouchViewPager : ViewPager {
    private var canTouch = false
    fun setCanTouch(canTouch: Boolean) {
        this.canTouch = canTouch
    }

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (canTouch) super.onTouchEvent(ev) else false
    }

    //@Override
    //public boolean onTouchEvent(MotionEvent arg0) {
    //  return false;
    //}
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (canTouch) super.onInterceptTouchEvent(ev) else false
    }

    //@Override
    //public boolean onInterceptTouchEvent(MotionEvent arg0) {
    //  return false;
    //}
    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        // TODO Auto-generated method stub
        if (canTouch) {
            super.setCurrentItem(item)
        } else {
            super.setCurrentItem(
                item,
                false
            ) //<span style="color: rgb(70, 70, 70); font-family: 'Microsoft YaHei', 'Helvetica Neue', SimSun; font-size: 14px; line-height: 21px; background-color: rgb(188, 211, 229);">表示切换的时候，不需要切换时间。</span>
        }
    }
}

package com.storen.autoclick.view

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

interface IWindowView {

    fun attachToWindow(wm: WindowManager)

    fun detachFromWindow()

    fun getWindowManager(): WindowManager?

    // 只需要声明，在View中已经有具体的实现
    fun getLayoutParams(): ViewGroup.LayoutParams

    fun setWinX(x: Int) {
        if (getLayoutParams() is WindowManager.LayoutParams) {
            (getLayoutParams() as WindowManager.LayoutParams).x = x
        }
    }

    fun setWinY(y: Int) {
        if (getLayoutParams() is WindowManager.LayoutParams) {
            (getLayoutParams() as WindowManager.LayoutParams).y = y
        }
    }

    fun getWinX(): Int {
        if (getLayoutParams() is WindowManager.LayoutParams) {
            return (getLayoutParams() as WindowManager.LayoutParams).x
        }
        return -1
    }

    fun getWinY(): Int {
        if (getLayoutParams() is WindowManager.LayoutParams) {
            return (getLayoutParams() as WindowManager.LayoutParams).y
        }
        return -1
    }

    fun updatePosition() {
        getWindowManager()?.updateViewLayout(this as View, layoutParams)
    }
}
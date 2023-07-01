package com.storen.autoclick.util

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue

object DensityExt {
    /**
     * 将一个浮点数，以dip为单位转换为对应的像素值
     */
    fun Float.dp2px(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this,
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * 将一个浮点数，以sp为单位转换为对应的像素值
     */
    fun Float.sp2px(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, this,
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * 将一个整数，以dip为单位转换为对应的像素值
     */
    fun Int.dp2px(): Float {
        return this.toFloat().dp2px()
    }

    /**
     * 将一个整数，以dip为单位转换为对应的像素值
     */
    fun Int.sp2px(): Float {
        return this.toFloat().sp2px()
    }

    /**
     * 获取状态栏高度
     * @return
     */
    fun Activity.getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    /*转换为像素*/
    inline val Int.dp: Float get() = this.dp2px()
    inline val Int.sp: Float get() = this.sp2px()
    inline val Float.dp: Float get() = this.dp2px()
    inline val Float.sp: Float get() = this.sp2px()
}
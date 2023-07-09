package com.storen.autoclick.view.impl

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.storen.autoclick.databinding.LayoutClickableBinding
import com.storen.autoclick.util.DensityExt.getCurrentWindowSize
import com.storen.autoclick.view.DragDelegate
import com.storen.autoclick.view.IWindowView

class ClickPointView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ImageFilterView(context, attrs), IWindowView {

    private var windowManager: WindowManager? = null

    private val touchDelegate by lazy { DragDelegate(this) }

    override fun attachToWindow(wm: WindowManager) {
        this.windowManager = wm
        windowManager?.addView(this, buildLayoutParams().apply {
            val currentWindowSize = wm.getCurrentWindowSize(resources)
            x = currentWindowSize.first / 3
            y = currentWindowSize.second / 3
        })
    }

    override fun detachFromWindow() {
        windowManager?.removeView(this)
    }

    override fun getWindowManager(): WindowManager? = windowManager

    private fun buildLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.START or Gravity.TOP
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE /*or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE*/
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 在部分界面无法显示
                /*LayoutParams.TYPE_APPLICATION_OVERLAY*/
                // 需搭配无障碍服务使用
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchDelegate.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> crossfade = 1f
            MotionEvent.ACTION_MOVE -> {}
            else -> crossfade = 0f
        }
        return false
    }

}
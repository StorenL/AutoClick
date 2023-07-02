package com.storen.autoclick

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

class ClickPointView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ImageFilterView(context, attrs) {

    private var downEventX: Float = 0f
    private var downEventY: Float = 0f
    private var downViewX: Int = 0
    private var downViewY: Int = 0

    private var windowManager: WindowManager? = null

    companion object {
        fun buildSelf(layoutInflater: LayoutInflater) = LayoutClickableBinding.inflate(layoutInflater).root
    }

    init {
        setOnLongClickListener {
            detachFromWindow()
            true
        }
    }

    fun attachToWindow(wm: WindowManager) {
        this.windowManager = wm
        windowManager?.addView(this, buildLayoutParams())
    }

    fun detachFromWindow() {
        windowManager?.removeView(this)
    }

    private fun buildLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.START or Gravity.TOP
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
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
        windowManager ?: return super.onTouchEvent(event)
        val params = layoutParams
        if (windowManager == null || params !is WindowManager.LayoutParams) return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                crossfade = 1f
                // 记录点击的位置不会丢失精度
                downEventX = event.rawX
                downEventY = event.rawY
                downViewX = params.x
                downViewY = params.y
            }

            MotionEvent.ACTION_MOVE -> {
                params.x = downViewX + (event.rawX - downEventX).toInt()
                params.y = downViewY + (event.rawY - downEventY).toInt()
                windowManager?.updateViewLayout(this, params)
            }

            else -> crossfade = 0f
        }
        return true
    }

}
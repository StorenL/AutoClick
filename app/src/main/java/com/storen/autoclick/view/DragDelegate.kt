package com.storen.autoclick.view

import android.view.MotionEvent

class DragDelegate(private val view: IWindowView) {

    private var downEventX: Float = 0f
    private var downEventY: Float = 0f
    private var downViewX: Int = 0
    private var downViewY: Int = 0

    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录点击的位置不会丢失精度
                downEventX = event.rawX
                downEventY = event.rawY
                downViewX = view.getWinX()
                downViewY = view.getWinY()
            }

            MotionEvent.ACTION_MOVE -> {
                view.setWinX(downViewX + (event.rawX - downEventX).toInt())
                view.setWinY(downViewY + (event.rawY - downEventY).toInt())
                view.updatePosition()
            }
        }
    }

}
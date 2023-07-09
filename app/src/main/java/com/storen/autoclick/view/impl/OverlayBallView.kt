package com.storen.autoclick.view.impl

import android.content.Context
import android.util.AttributeSet
import android.view.WindowManager
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.storen.autoclick.view.IWindowView

class OverlayBallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ImageFilterView(context, attrs), IWindowView {
    override fun attachToWindow(wm: WindowManager) {
        TODO("Not yet implemented")
    }

    override fun detachFromWindow() {
        TODO("Not yet implemented")
    }

    override fun getWindowManager(): WindowManager? {
        TODO("Not yet implemented")
    }

    override fun updatePosition() {
        TODO("Not yet implemented")
    }


}
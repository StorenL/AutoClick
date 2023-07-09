package com.storen.autoclick.view

import android.view.LayoutInflater
import com.storen.autoclick.databinding.LayoutClickableBinding

object WindowViewFactory {

    enum class ViewType { POINT, BALL }

    fun getWindowView(type: ViewType, inflater: LayoutInflater): IWindowView {
        return when(type) {
            ViewType.POINT -> LayoutClickableBinding.inflate(inflater).root
            ViewType.BALL -> LayoutClickableBinding.inflate(inflater).root
        }
    }

}
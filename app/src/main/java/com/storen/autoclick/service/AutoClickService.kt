package com.storen.autoclick.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.storen.autoclick.ClickPointView
import com.storen.autoclick.util.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@AndroidEntryPoint
class AutoClickService : AccessibilityService() {

    private lateinit var coroutineScope: CoroutineScope

    private val layoutInflater by lazy { LayoutInflater.from(this) }

    private val addedViewList: MutableList<View> = mutableListOf()

    private val windowManager by lazy { getSystemService(WindowManager::class.java) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onServiceConnected() {
        super.onServiceConnected()
        "onServiceConnected".log()
        ClickPointView.buildSelf(layoutInflater).attachToWindow(windowManager)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.log()
        rootInActiveWindow?.log() ?: return
        val boundRect = Rect()
        for (i in 0 until rootInActiveWindow.childCount) {
            val child = rootInActiveWindow.getChild(i) ?: break
            child.log()
            child.getBoundsInScreen(boundRect)
            boundRect.log()
        }
    }

    override fun onInterrupt() {
        "AccessibilityService interrupted.".log()
        coroutineScope.cancel()
    }

    override fun onCreate() {
        super.onCreate()
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
package com.storen.autoclick.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.storen.autoclick.util.log

class AutoClickService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.log()
        rootInActiveWindow.log()
        for (i in 0 until rootInActiveWindow.childCount) {
            rootInActiveWindow.getChild(i).log()
        }
    }

    override fun onInterrupt() {
        "AccessibilityService interrupted.".log()
    }
}
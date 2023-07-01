package com.storen.autoclick.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import java.lang.Exception

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        /*在匿名内部类中获取不到类名，这将导致Log无法显示，故有此判断*/
        return if (tag.isBlank()) "#anonymous#" else if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Any.log(tag: String = TAG) {
    Log.d(tag, this.toString())
}

fun Any.toast(context: Context) {
    Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT).show()
}

fun View.clip2Round(radius: Float) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    clipToOutline = true
}

fun Context.openAccessibilitySettings() =
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })

fun Context.openSystemSettings() = startActivity(Intent(Settings.ACTION_SETTINGS).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })

val overLayoutSettingsIntent: Intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
fun Context.openOverlaySettings() = startActivity(overLayoutSettingsIntent)

fun Context.tryOpenSettings(block: () -> Unit) = try {
    block()
} catch (ignore: Exception) {
    openSystemSettings()
}

fun Context.canDrawOverlays() = Settings.canDrawOverlays(this)

fun Context.isAccessibilityOpen(accessibilityManager: AccessibilityManager): Boolean {
    return accessibilityManager
        .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        .firstOrNull {
            TextUtils.equals(it.resolveInfo.serviceInfo.packageName, packageName)
        } != null
}


package com.storen.autoclick.util

import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Toast

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

fun Context.openAccessibilitySetting() = startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
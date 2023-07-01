package com.storen.autoclick

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.storen.autoclick.util.canDrawOverlays
import com.storen.autoclick.util.isAccessibilityOpen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var accessibilityManager: AccessibilityManager

    private val _isAccessibilityEnable = MutableLiveData<Boolean>()
    val isAccessibilityEnable: LiveData<Boolean> get() = _isAccessibilityEnable

    private val _isOverlayEnable = MutableLiveData<Boolean>()
    val isOverlayEnable get() = _isOverlayEnable

    fun checkAccessibilityAndOverlayStatus(context: Context) {
        _isAccessibilityEnable.postValue(context.isAccessibilityOpen(accessibilityManager))
        _isOverlayEnable.postValue(context.canDrawOverlays())
    }

}
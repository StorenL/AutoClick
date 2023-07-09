package com.storen.autoclick

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
    val isOverlayEnable: LiveData<Boolean> get() = _isOverlayEnable

    private val _canAddViewLiveData = MediatorLiveData<Boolean>().apply {
        addSource(isAccessibilityEnable) {
            value = isOverlayEnable.value == true && it
        }
        addSource(isOverlayEnable) {
            value = isAccessibilityEnable.value == true && it
        }
    }
    val canAddViewLiveData: LiveData<Boolean> get() = _canAddViewLiveData

    private val _isMediaProjectionEnable = MutableLiveData<Boolean>()
    val isMediaProjectionEnable get() = _isMediaProjectionEnable

    fun checkPermissionStatus(context: Context, isMediaProjectionEnable: Boolean) {
        _isAccessibilityEnable.postValue(context.isAccessibilityOpen(accessibilityManager))
        _isOverlayEnable.postValue(context.canDrawOverlays())
        _isMediaProjectionEnable.postValue(isMediaProjectionEnable)
    }

}
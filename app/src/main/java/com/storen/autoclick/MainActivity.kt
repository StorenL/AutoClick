package com.storen.autoclick

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.storen.autoclick.base.BaseActivity
import com.storen.autoclick.databinding.ActivityMainBinding
import com.storen.autoclick.util.toast
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: MainViewModel by viewModels()

    override fun ActivityMainBinding.beforeInit() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkAccessibilityAndOverlayStatus(this@MainActivity)
        }
        OpenCVLoader.initDebug().toast(this@MainActivity)
    }

    override fun ActivityMainBinding.initView() {


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun ActivityMainBinding.initClick() {
        btnOpenPermission1.setOnClickListener {
            activityResultLauncher.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        btnOpenPermission2.setOnClickListener {
            activityResultLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }
        btnAddClickView.setOnClickListener {

        }
    }

    override fun ActivityMainBinding.observeData() {
        viewModel.isAccessibilityEnable.observe(this@MainActivity) {
            btnOpenPermission1.isEnabled = !it
        }

        viewModel.isOverlayEnable.observe(this@MainActivity) {
            btnOpenPermission2.isEnabled = !it
        }
    }

    override fun ActivityMainBinding.afterCreate() {
        viewModel.checkAccessibilityAndOverlayStatus(this@MainActivity)
    }

}
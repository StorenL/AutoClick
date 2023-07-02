package com.storen.autoclick

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.storen.autoclick.base.BaseActivity
import com.storen.autoclick.databinding.ActivityMainBinding
import com.storen.autoclick.util.DensityExt.getCurrentWindowSize
import com.storen.autoclick.util.toast
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var appWindowManager: WindowManager

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val mediaProjectionManager: MediaProjectionManager by lazy { getSystemService(MediaProjectionManager::class.java) }
    private lateinit var mediaProjection: MediaProjection
    private var virtualDisplay: VirtualDisplay? = null

    override fun ActivityMainBinding.beforeInit() {
        // 注册StartForActivity，必须在onCreate方法中
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { mediaProjection = mediaProjectionManager.getMediaProjection(result.resultCode, it) }
            viewModel.checkPermissionStatus(this@MainActivity, ::mediaProjection.isInitialized)
        }
    }

    override fun ActivityMainBinding.initView() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun ActivityMainBinding.initClick() {
        btnAction1.setOnClickListener {
            activityResultLauncher.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        btnAction2.setOnClickListener {
            activityResultLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }
        btnAction3.setOnClickListener {
            if (viewModel.isAccessibilityEnable.value != true) {
                "Must open accessibility first.".toast(this@MainActivity)
                btnAction1.callOnClick()
                return@setOnClickListener
            }
            activityResultLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
        }
        btnAction4.setOnClickListener {
            btnAction4.isEnabled = !OpenCVLoader.initDebug()
        }
        btnAction5.setOnClickListener {
            if (viewModel.isAccessibilityEnable.value != true || viewModel.isMediaProjectionEnable.value != true) {
                "Some config isn't enable.".toast(this@MainActivity)
                return@setOnClickListener
            }
            if (virtualDisplay == null) {
                val currentWindowSize = appWindowManager.getCurrentWindowSize(resources)
                virtualDisplay = mediaProjection.createVirtualDisplay(
                    "ScreenCapture",
                    currentWindowSize.first,
                    currentWindowSize.second,
                    currentWindowSize.third,
                    VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    surfaceView.holder.surface,
                    null,
                    null
                )
            } else {
                virtualDisplay?.release()
                virtualDisplay = null
            }
        }
    }

    override fun ActivityMainBinding.observeData() {
        viewModel.isAccessibilityEnable.observe(this@MainActivity) {
            btnAction1.isEnabled = !it
        }
        viewModel.isOverlayEnable.observe(this@MainActivity) {
            btnAction2.isEnabled = !it
        }
        viewModel.isMediaProjectionEnable.observe(this@MainActivity) {
            btnAction3.isEnabled = !it
        }
    }

    override fun ActivityMainBinding.afterCreate() {
        viewModel.checkPermissionStatus(this@MainActivity, ::mediaProjection.isInitialized)
    }

}
package com.storen.autoclick.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import com.storen.autoclick.view.impl.ClickPointView
import com.storen.autoclick.constant.NotificationConst.FOREGROUND_NOTIFICATION_ID
import com.storen.autoclick.constant.NotificationConst.NOTIFICATION_CHANNEL_ID
import com.storen.autoclick.util.log
import com.storen.autoclick.view.IWindowView
import com.storen.autoclick.view.WindowViewFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * 无障碍模式服务和屏幕录制服务合二为一
 */
@AndroidEntryPoint
class AutoClickService : AccessibilityService() {

    companion object Command {
        private const val COMMAND_TYPE = "command_type"
        private const val COMMAND_TYPE_BALL = 1

        fun overlayBall(ctx: Context) = Intent(ctx, AutoClickService::class.java).apply {
            putExtra(COMMAND_TYPE, COMMAND_TYPE_BALL)
        }
    }

    private lateinit var coroutineScope: CoroutineScope

    private val layoutInflater by lazy { LayoutInflater.from(this) }

    private val addedViewList: MutableList<IWindowView> = mutableListOf()

    private val windowManager by lazy { getSystemService(WindowManager::class.java) }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val overlayBall = intent.getIntExtra(COMMAND_TYPE, 0)
        if (overlayBall == COMMAND_TYPE_BALL) {
            addClickableView()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun addClickableView() {
        WindowViewFactory.getWindowView(WindowViewFactory.ViewType.POINT, layoutInflater).let {
            it.attachToWindow(windowManager)
            addedViewList.add(it)
        }
    }
    /**
     * 当前Service成功绑定系统无障碍服务时的回调
     */
    override fun onServiceConnected() {
        super.onServiceConnected()
        "onServiceConnected".log()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // event?.log()
        // rootInActiveWindow?.log() ?: return
        // val boundRect = Rect()
        // for (i in 0 until rootInActiveWindow.childCount) {
        //     val child = rootInActiveWindow.getChild(i) ?: break
        //     child.log()
        //     child.getBoundsInScreen(boundRect)
        //     boundRect.log()
        // }
        // 应该可以在这里遍历出所有的Not，然后绘制边框
    }
    override fun onCreate() {
        super.onCreate()
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        startForeground(FOREGROUND_NOTIFICATION_ID, NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).build())
    }

    /**
     * 无障碍服务的中断，不了解其触发场景，暂且当作onDestroy处理
     */
    override fun onInterrupt() {
        "AccessibilityService interrupted.".log()
        addedViewList.forEach { it.detachFromWindow() }
        addedViewList.clear()
        coroutineScope.cancel()
    }

    /**
     * 服务异常退出可能来不及调用onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        "Service onDestroy".log()
        addedViewList.forEach { it.detachFromWindow() }
        addedViewList.clear()
        coroutineScope.cancel()
    }
}
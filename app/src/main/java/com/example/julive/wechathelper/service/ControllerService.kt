package com.example.julive.wechathelper.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
import com.example.julive.wechathelper.ext.*
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.longToast
import java.io.File
import java.lang.Exception
import java.util.*

class ControllerService : AccessibilityService() {

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventType = event?.eventType
        val className = event?.className
        val text = event?.text
        val contentDescription = event?.contentDescription
        val sourceText = event?.source?.text
        val sourceViewId = event?.source?.viewIdResourceName

        if (event?.source != null) {
            log(
                content =
                "EventType=${AccessibilityEvent.eventTypeToString(eventType ?: 0)} " +
                        "ClassName=$className " +
                        "Text=$text " +
                        "ContentDescription=$contentDescription " +
                        "NodeInfo.text=$sourceText" +
                        "NodeInfo.text=$sourceViewId"
            )

        } else {
            log(
                content =
                "EventType=${AccessibilityEvent.eventTypeToString(eventType ?: 1)} " +
                        "ClassName=$className " +
                        "Text=$text " +
                        "ContentDescription=$contentDescription "
            )
        }
        when (eventType) {
            TYPE_WINDOW_CONTENT_CHANGED -> {
                rootInActiveWindow?.printAllChild()
            }
        }
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            log("ControllerService", "-------------------------------" + text?.toString())
            if (text?.toString()?.contains("小哆亮屏") == true) {
                wakeUpAndUnlock()
            }
            if (text?.toString()?.contains("小哆解锁") == true) {
                val result = Shell.su("input swipe 500 1000 500 50").exec()
                log("ControllerService", "解锁-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆右滑") == true) {
                val result = Shell.su("input swipe 50 500 800 500").exec()
                log("ControllerService", "小哆右滑-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆左滑") == true) {
                val result = Shell.su("input swipe 800 500 50 500").exec()
                log("ControllerService", "小哆左滑-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆上滑") == true) {
                val result = Shell.su("input swipe 500 1000 500 50").exec()
                log("ControllerService", "小哆上滑-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆下滑") == true) {
                val result = Shell.su("input swipe 500 50 500 1000").exec()
                log("ControllerService", "小哆下滑-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆锁屏") == true) {
                val result = Shell.su("input keyevent 26").exec()
                log("ControllerService", "锁屏-------------------------------${result.isSuccess}" + result.err)
            }
            if (text?.toString()?.contains("小哆打开微信") == true) {
                Shell.su("am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI").exec()
                runBlocking {
                    delay(2000)
                    performHomeClick { }
                }
            }
            if (text?.toString()?.contains("小哆返回") == true) {
                performBackClick { }
            }
            if (text?.toString()?.contains("小哆home") == true) {
                performHomeClick { }
            }
            if (text?.toString()?.contains("小哆任务列表") == true) {
                performMenuClick { }
            }
            if (text?.toString()?.contains("小哆打开相机") == true) {
                Shell.su("am start -n com.android.camera/com.android.camera.Camera").exec()
            }
            if (text?.toString()?.contains("小哆打开浏览器") == true) {
                Shell.su("am start -n com.android.browser/com.android.browser.BrowserActivity").exec()
            }
            if (text?.toString()?.contains("小哆截屏分享") == true) {
                val fileName = UUID.randomUUID().toString()
                Shell.su("mkdir /sdcard/screencap").exec()
                val result = Shell.su("screencap -p /sdcard/screencap/$fileName.png").exec()
                if (result.isSuccess) {
                    longToast("截图成功，保存至:/sdcard/screencap/$fileName.png")
                    sharePictureToTimeLine(File("/sdcard/screencap/$fileName.png"))
                }
            }
            if (text?.toString()?.contains("小哆点击") == true) {
                try {
                    val txt = text.toString()
                    val clickName = txt.substring(txt.indexOf("点击") + 2, txt.length - 1)
                    val realName = clickName.trim()
                    val rootWindow = rootInActiveWindow
                    if (rootWindow != null) {
                        rootWindow.findByText(realName)?.clickParent { }
                        rootWindow.findByText(realName)?.click { }
                    }
                } catch (e: Exception) {
                    longToast("点击失败:${e.message}")
                }
            }
        }
    }
}
package com.example.julive.wechathelper.ext

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.julive.wechathelper.service.AutoCloseXmAdvertisementService

private val handler = Handler()

fun AccessibilityNodeInfo.click(millis: Long = 1000, func: ((isSuccess: Boolean) -> Unit)? = null) {
    nLog()
    handler.postDelayed({
        if (isClickable && performAction(AccessibilityNodeInfo.ACTION_CLICK))
            func?.invoke(true)
        else
            func?.invoke(false)
        recycle()
    }, millis)
}

fun AccessibilityNodeInfo.clickParent(millis: Long = 1000, func: ((isSuccess: Boolean) -> Unit)? = null) {
    parent?.nLog()
    handler.postDelayed({
        if (parent?.isClickable == true && parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true)
            func?.invoke(true)
        else
            func?.invoke(false)
        recycle()
    }, millis)
}

fun AccessibilityNodeInfo.forward(millis: Long = 2000, func: ((Boolean) -> Unit)? = null) {
    handler.postDelayed({
        func?.invoke(performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD))
    }, millis)
}

fun AccessibilityNodeInfo.backward(millis: Long = 1000, func: (() -> Unit)? = null) {
    handler.postDelayed({
        performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
        func?.invoke()
    }, millis)
}

fun AccessibilityNodeInfo.findByText(text: String = "", index: Int = 0): AccessibilityNodeInfo? {
    return try {
        val list = findAccessibilityNodeInfosByText(text)
        if (list.isNotEmpty() && index < list.size) list[index].nLog() else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun AccessibilityNodeInfo.findById(id: String = "", index: Int = 0): AccessibilityNodeInfo? {
    return try {
        val list = findAccessibilityNodeInfosByViewId(id)
        if (list.isNotEmpty() && index < list.size) list[index].nLog() else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun AccessibilityNodeInfo.printAllChild() {
    (0 until childCount).forEach {
        val child = getChild(it)
        log(
            "printAllChild",
            content =
            "className${child?.className} viewIdResourceName${child?.viewIdResourceName} text${child?.text} childCount${child?.childCount} contentDescription${child?.contentDescription}"
        )
        child?.printAllChild()
    }
}


fun AccessibilityNodeInfo.nLog(): AccessibilityNodeInfo {
    log(
        content = "ClassName=$className " +
                "Text=$text " +
                "ContentDescription=$contentDescription " +
                "isClickable=$isClickable " +
                "childCount=$childCount"
    )
    return this
}

fun log(tag: String = "NodeInfoTest", content: String) {
    Log.d(tag, content)
}

/**
 * 打开通知栏消息
 */
fun AccessibilityEvent.openNotification(millis: Long = 1000, func: ((isSuccess: Boolean) -> Unit)? = null) {
    if (parcelableData == null || parcelableData !is Notification) {
        func?.invoke(false)
        return
    }
    //将通知栏消息打开
    val notification = parcelableData as Notification
    val pendingIntent = notification.contentIntent
    try {
        handler.postDelayed({
            pendingIntent.send()
            func?.invoke(true)
        }, millis)
    } catch (e: PendingIntent.CanceledException) {
        e.printStackTrace()
        func?.invoke(false)
    }
}

fun Boolean.isSuccess(success: () -> Unit, fail: (() -> Unit)? = null) {
    if (this)
        success()
    else
        fail?.invoke()
}


fun Context.isAccessibilityServiceSettingEnabled(): Boolean {
    val service = packageName + "/" + AutoCloseXmAdvertisementService::class.java.canonicalName
    val accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
    if (accessibilityEnabled != 1)
        return false
    val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
    val settingValue = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

    mStringColonSplitter.setString(settingValue)
    while (mStringColonSplitter.hasNext()) {
        val accessibilityService = mStringColonSplitter.next()
        if (accessibilityService.equals(service, ignoreCase = true)) {
            return true
        }
    }
    return false
}

const val AutoPrintRedPackageService =
    "com.example.julive.wechathelper/com.example.julive.wechathelper.service.AutoPrintRedPackageService"

const val ControllerService =
    "com.example.julive.wechathelper/com.example.julive.wechathelper.service.ControllerService"

fun Context.isAccessibilityServiceEnabled(serviceName: String = AutoPrintRedPackageService): Boolean {
//    val accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
//    logD("isAccessibilityServiceSettingEnabled", "accessibilityEnabled $accessibilityEnabled")
//    if (accessibilityEnabled != 1)
//        return false
    val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
    val settingValue = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    mStringColonSplitter.setString(settingValue)
    while (mStringColonSplitter.hasNext()) {
        val accessibilityService = mStringColonSplitter.next()
        if (accessibilityService.equals(serviceName, ignoreCase = true)) {
            log("isAccessibilityServiceEnabled", "true")
            return true
        }
    }
    log("isAccessibilityServiceEnabled", "false")
    return false
}

/**
 * 开启无障碍服务
 */
fun Context.startAccessibilityService(serviceName: String = ControllerService) {
    if (!isAccessibilityServiceEnabled(serviceName)) {
        Settings.Secure.putString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, serviceName)
        Settings.Secure.putInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 1)
        log(
            "isAccessibilityServiceEnabled",
            "ACCESSIBILITY_ENABLED=========================================$serviceName 开启中"
        )
    } else {
        log(
            "isAccessibilityServiceEnabled",
            "ACCESSIBILITY_ENABLED========================================$serviceName  已开启"
        )
    }
}

/**
 * 点击回退按钮
 */
fun AccessibilityService.performBackClick(func: ((isSuccess: Boolean) -> Unit)? = null) {
    handler.postDelayed({
        func?.invoke(performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK))
    }, 1300L)
}

/**
 * 回主页
 */
fun AccessibilityService.performHomeClick(func: ((isSuccess: Boolean) -> Unit)? = null) {
    handler.postDelayed({
        func?.invoke(performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME))
    }, 1300L)
}

/**
 * 点击菜单按钮
 */
fun AccessibilityService.performMenuClick(func: ((isSuccess: Boolean) -> Unit)? = null) {
    handler.postDelayed({
        func?.invoke(performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS))
    }, 1300L)
}

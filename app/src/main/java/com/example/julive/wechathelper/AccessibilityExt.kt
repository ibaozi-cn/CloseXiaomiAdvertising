package com.example.julive.wechathelper

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

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
    (0 until childCount).forEach { it ->
        val child = getChild(it)
        log(
            content =
            "className${child?.className} viewIdResourceName${child?.viewIdResourceName} text${child?.text} childCount${child?.childCount} contentDescription${child?.contentDescription}"
        )
        child.printAllChild()
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
    val service = packageName + "/" + WechatService::class.java.canonicalName
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
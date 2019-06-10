package com.example.julive.wechathelper

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoPrintRedPackageService : AccessibilityService() {

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
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            wakeUpAndUnlock(this@AutoPrintRedPackageService)
            if (text.toString().contains("微信红包")) {
            }
        }
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {

            }
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> {
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                if (className == "android.widget.ListView") {
                    val node = event.source
                    if (node != null) {
                        //红包详情界面
                        if (node.viewIdResourceName == "com.tencent.mm:id/cvt") {
                            //获取红包内容
                            getChildFromNode(event.source)
                        }
                    }
                }
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if (className == "com.tencent.mm.ui.LauncherUI") {
                    val node = event.source
                    if (node != null) {
                        //聊天输入框
                        if (mapMoneyList.isEmpty()) {
                            return
                        }
                        val itemList = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ami")
                        if (itemList != null && !itemList.isEmpty()) {
                            val edit = itemList[0]
                            val moneyStr = StringBuilder()
                            log("mapMoneyList", mapMoneyList.toString())
                            mapMoneyList.forEach {
                                val value = it.value
                                val num = value.replace("元", "")
                                if (num.endsWith(endNumber))
                                    moneyStr.append(it.key).append(": ").append(it.value).append("\n")
                            }
                            val arguments = Bundle()
                            arguments.putCharSequence(
                                AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, moneyStr.toString()
                            )
                            edit.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                            edit.performAction(AccessibilityNodeInfo.ACTION_SELECT)
                            edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                            mapMoneyList.clear()
                        }
                    }
                }

                if (className == "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI") {

                }
                //打开红包详细界面自动识别逻辑
                if (className == "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI") {
                    val sourceCount = event.source?.childCount ?: 0
                    val source = event.source
                    (0 until sourceCount).forEach {
                        val child = source.getChild(it)
                        if (child?.className == "android.widget.ListView") {
                            val items = child.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cv0")
                            if (items != null && !items.isEmpty()) {
                                val item = items[0]
                                val text = item.text
                                if (text.contains("/")) {
                                    val array = text.split("/")
                                    val total = array[0]
                                    val count = total.replace("领取", "").replace("已", "").toInt()
                                    val size = (count + 16) / 8
                                    log("childDetail", "ACTION_SCROLL_FORWARD size====$size")
                                    child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                                    child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)

                                } else {
                                    if (text.contains("个红包")) {
                                        val array = text.split("个")
                                        val total = array[0].toInt()
                                        val size = (total + 16) / 8
                                        log("childDetail", "ACTION_SCROLL_FORWARD size====$size")
                                        child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                                        child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                //查找微信聊天中的微信红包
                if (className == "android.widget.ListView") {
                    val node = event.source
                    getChildFromNode(node)
                }
            }
        }
    }

    private val mapMoneyList = mutableMapOf<String, String>()
    private var endNumber = ""
    private fun getChildFromNode(node: AccessibilityNodeInfo?) {
        val itemCount = node?.childCount ?: 0
        (0 until itemCount).forEach { it ->
            val child = node?.getChild(it)
            if (child?.viewIdResourceName == "com.tencent.mm:id/d06") {
                mapMoneyList[child.text.toString()] = node.getChild(it + 1).text.toString()
            }
            if (child?.viewIdResourceName == "com.tencent.mm:id/aqj") {
                child.text.toString().forEach {
                    if ("[0-9]+".toRegex().matches(it.toString())) {
                        endNumber = it.toString()
                        return
                    }
                }
            }
            getChildFromNode(node?.getChild(it))
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    fun wakeUpAndUnlock(context: Context) {
        // 获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val screenOn = pm.isScreenOn
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            val wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright"
            )
            wl.acquire(10000) // 点亮屏幕
            wl.release() // 释放
        }
        // 屏幕解锁
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock("unLock")
        // 屏幕锁定
        keyguardLock.reenableKeyguard()
        keyguardLock.disableKeyguard() // 解锁
    }
}
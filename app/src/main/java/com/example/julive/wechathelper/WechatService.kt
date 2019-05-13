package com.example.julive.wechathelper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.github.rubensousa.viewpagercards.FileUtil
import com.github.rubensousa.viewpagercards.logPath


class WechatService : AccessibilityService() {

    private val handler = Handler()

    override fun onInterrupt() {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkgName = event?.packageName.toString()
        val eventType = event?.eventType
        val className = event?.className
        val config = FileUtil.readLogByString(logPath, "0")
        log("className==\"$className\"" + "pkgName===$pkgName" + "config==$config")
        when (eventType) {
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> {
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if (className == "com.miui.securityscan.ui.settings.SettingsActivity") {
                    clickForBox("推荐内容", time = 500L)
                    clickForBox("仅在WLAN下推荐", time = 500L)
                    FileUtil.writeLog(logPath, "1", false, "utf-8")
                }
                if (className == "com.android.providers.downloads.ui.DownloadList") {
                }

                if (className == "com.android.settings.SubSettings") {
                    if (config == "1000")
                        backOneFirst()
                }
                if (className == "com.example.julive.wechathelper.MainActivity") {
                    if (config == "2000") {
                        clickById("com.example.julive.wechathelper:id/layout")
                        FileUtil.writeLog(logPath, "0", false, "utf-8")
                    }
                }
                if(className=="com.example.julive.wechathelper.SettingActivity"){

                }
                if (className == "com.miui.optimizecenter.settings.SettingsActivity") {
                    FileUtil.writeLog(logPath, "2", false, "utf-8")
                }
                if (className == "com.miui.appmanager.AppManagerSettings") {
                    FileUtil.writeLog(logPath, "3", false, "utf-8")
                }
                if (className == "com.xiaomi.market.ui.MarketPreferenceActivity") {
                    FileUtil.writeLog(logPath, "4", false, "utf-8")
                }
                if (className == "com.android.providers.downloads.ui.activity.InfoFlowSettingActivity") {
                    FileUtil.writeLog(logPath, "5", false, "utf-8")
                }
                if (className == "com.miui.video.feature.mine.setting.SettingActivity") {
                    FileUtil.writeLog(logPath, "6", false, "utf-8")
                }
                if (className == "com.miui.player.phone.ui.MusicSettings") {
                    FileUtil.writeLog(logPath, "7", false, "utf-8")
                }
                if (className == "com.android.calendar.settings.UserExperiencePreferencesActivity") {
                    FileUtil.writeLog(logPath, "8", false, "utf-8")
                }
                if (className == "com.miui.weather2.ActivitySet") {
                    FileUtil.writeLog(logPath, "9", false, "utf-8")
                }
                if (className == "com.android.quicksearchbox.preferences.SearchSettingsPreferenceActivity") {
                    FileUtil.writeLog(logPath, "10", false, "utf-8")
                }
                if (className == "com.miui.home.settings.MiuiHomeSettingActivity") {
                    FileUtil.writeLog(logPath, "11", false, "utf-8")
                }
                if (className == "com.sohu.inputmethod.sogou.xiaomi.SogouIMESettings") {
                    FileUtil.writeLog(logPath, "12", false, "utf-8")
                }
//                if (className == "com.github.rubensousa.viewpagercards.MainPageActivity") {
//                    if (config == "12") {
//                        FileUtil.writeLog(logPath, "0", false, "utf-8")
//                    }
//                }
                if (className == "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity") {
                    FileUtil.writeLog(logPath, "0", false, "utf-8")
                }

                if (className == "com.android.providers.downloads.ui.DownloadList") {
                    val nodeInfo = rootInActiveWindow
                    if (nodeInfo == null) {
                        Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                    }
                    val node = nodeInfo.findAccessibilityNodeInfosByText("下载管理")
                    if (node != null) {
                        val cout = node.count()
                        (0 until cout).forEach {
                            val self = node[it]
//                            logT("parent==" + self?.className.toString() + self?.text)
                            if (self != null) {
                                val parent = self.parent
                                val count = parent.childCount
                                (0 until count).forEach { i ->
                                    val child = parent.getChild(i)
//                                    logT("child==" + child?.className.toString())
                                    if (child?.className.toString() == "android.widget.ImageView" && i == 3) {
                                        child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                        click("设置", time = 500L)
                                        click("信息流设置", time = 1500L)
                                        clickForBox("仅在WLAN下加载", time = 2500L)
                                        clickForBox("资源推荐", time = 2500L)
                                        click("确定", time = 3500L)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun backOneFirst() {
        FileUtil.writeLog(logPath, "2000", false, "utf-8")
        performBackClick(100)
        performBackClick(1000)
        performBackClick(2000)
    }

    /**
     * 关闭监控
     */
    private fun resetConfig() {
        FileUtil.writeLog(logPath, "0", false, "utf-8")
        stopSelf() //走完一次流程 关闭自己，防止一直监控
    }

    /**
     * 点击匹配的nodeInfo
     * @param str text关键字
     */
    private fun clickForBox(str: String, action: Int = AccessibilityNodeInfo.ACTION_CLICK, time: Long) {
        handler.postDelayed({
            val nodeInfo = rootInActiveWindow
            if (nodeInfo == null) {
                Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            val list = nodeInfo.findAccessibilityNodeInfosByText(str)
            log(list.toString())
            if (list != null && list.size > 0) {
                val view = list[list.size - 1]
//                view.performAction(action)
//                view.parent?.performAction(action)
                val parent = view.parent
                if (parent != null) {
                    val count = parent.childCount
                    (0 until count).forEach { i ->
                        val child = parent.getChild(i)
                        logT("child==" + child?.className.toString() + child.isChecked)
                        if (child?.className == "android.widget.CheckBox") {
                            if (child.isChecked) {
                                child.performAction(action)
                                child.parent?.performAction(action)
                            }
                        }
                    }
                }
            } else {
//                Toast.makeText(this, "click 找不到有效的节点", Toast.LENGTH_SHORT).show()
            }
            nodeInfo.recycle()
        }, time)
    }

    /**
     * 点击匹配的nodeInfo
     * @param str text关键字
     */
    private fun click(str: String, action: Int = AccessibilityNodeInfo.ACTION_CLICK, time: Long) {
        handler.postDelayed({
            val nodeInfo = rootInActiveWindow
            if (nodeInfo == null) {
                Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            val list = nodeInfo.findAccessibilityNodeInfosByText(str)
            log(list.toString())
            if (list != null && list.size > 0) {
                val view = list[list.size - 1]
                view.performAction(action)
                view.parent?.performAction(action)
            } else {
//                Toast.makeText(this, "click 找不到有效的节点", Toast.LENGTH_SHORT).show()
            }
            nodeInfo.recycle()
        }, time)
    }

    /**
     * 点击匹配的nodeInfo
     * @param str text关键字
     */
    private fun close(str: String, action: Int = AccessibilityNodeInfo.ACTION_CLICK) {
        handler.postDelayed({
            val nodeInfo = rootInActiveWindow
            if (nodeInfo == null) {
                Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            val list = nodeInfo.findAccessibilityNodeInfosByText(str)
            log(list.toString())
            if (list != null && list.size > 0) {
                val view = list[list.size - 1]
                view.performAction(action)
                view.parent?.performAction(action)
            } else {
//                Toast.makeText(this, "click 找不到有效的节点", Toast.LENGTH_SHORT).show()
            }
            nodeInfo.recycle()
        }, 1000)
    }


    /**
     * 点击匹配的nodeInfo
     * @param str text关键字
     */
    private fun clickById(str: String, action: Int = AccessibilityNodeInfo.ACTION_CLICK) {
        handler.postDelayed({
            val nodeInfo = rootInActiveWindow
            if (nodeInfo == null) {
                Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            val list = nodeInfo.findAccessibilityNodeInfosByViewId(str)
            log(list.toString())
            if (list != null && list.size > 0) {
                list[list.size - 1].performAction(action)
                list[list.size - 1].parent?.performAction(action)
            } else {
//                Toast.makeText(this, "clickById 找不到有效的节点", Toast.LENGTH_SHORT).show()
            }
            nodeInfo.recycle()
        }, 1000)
    }

    //自动输入打招呼内容
    private fun input(hello: String) {
        handler.postDelayed({
            val nodeInfo = rootInActiveWindow
            if (nodeInfo == null) {
                Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            //找到当前获取焦点的view
            val target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)

            if (target == null) {
                log("input: null")
                return@postDelayed
            }
            val arguments = Bundle()
            arguments.putCharSequence(
                AccessibilityNodeInfo
                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, hello
            )
            target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            nodeInfo.recycle()
        }, 1000)
    }

    private fun log(config: String?) {
        Log.d("AccessibilityNodeInfo", config)
    }

    private fun logT(config: String?) {
        Log.d("logT", config)
    }

    /**
     * 打开通知栏消息
     */
    private fun openNotification(event: AccessibilityEvent) {
        if (event.parcelableData == null || event.parcelableData !is Notification) {
            return
        }
        //将通知栏消息打开
        val notification = event.parcelableData as Notification
        val pendingIntent = notification.contentIntent
        try {
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }
    }

    /**
     * 点击回退按钮
     */
    private fun performBackClick(dealyMillis: Long) {
        handler.postDelayed({ performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK) }, dealyMillis)
    }

    /**
     * 回主页
     */
    private fun performHomeClick() {
        handler.postDelayed({
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
        }, 1300L)
    }

    /**
     * 点击菜单按钮
     */
    private fun performMenuClick() {
        handler.postDelayed({
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
        }, 1300L)
    }

    /**
     * 控制该动作只操作一次
     */
    private var isOneTime = true

    /**
     * 点击菜单按钮后一秒再点击按钮返回
     * 目的为了刷新当前页面，拿到当前页根节点
     */
    private fun performMenuDoubleClick(doubleCallBack: () -> Unit) {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
        handler.postDelayed({
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            doubleCallBack()
            isOneTime = false
        }, 1000)
    }

    /**
     * 点击选项框
     */
    private fun performClickBtn(accessibilityNodeInfoList: List<AccessibilityNodeInfo>?): Boolean {
        if (accessibilityNodeInfoList != null && accessibilityNodeInfoList.isNotEmpty()) {
            for (i in accessibilityNodeInfoList.indices) {
                val accessibilityNodeInfo = accessibilityNodeInfoList[i]
                if (accessibilityNodeInfo.isClickable && accessibilityNodeInfo.isEnabled) {
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return true
                }
            }
        }
        return false
    }

    /**
     * 选择图片
     *
     * @param startPicIndex 从第startPicIndex张开始选
     * @param picCount      总共选picCount张
     */
    private fun choosePicture(startPicIndex: Int, picCount: Int) {
        handler.postDelayed({
            val accessibilityNodeInfo = rootInActiveWindow
            if (accessibilityNodeInfo == null) {
                Toast.makeText(this, "accessibilityNodeInfo is null", Toast.LENGTH_SHORT).show()
                return@postDelayed
            }
            val accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("预览")
            if (accessibilityNodeInfoList == null ||
                accessibilityNodeInfoList.size == 0 ||
                accessibilityNodeInfoList[0].parent == null ||
                accessibilityNodeInfoList[0].parent.childCount == 0
            ) {
                return@postDelayed
            }
            val tempInfo = accessibilityNodeInfoList[0].parent.getChild(3)

            for (j in startPicIndex until startPicIndex + picCount) {
                val childNodeInfo = tempInfo.getChild(j)
                if (childNodeInfo != null) {
                    for (k in 0 until childNodeInfo.childCount) {
                        if (childNodeInfo.getChild(k).isEnabled && childNodeInfo.getChild(k).isClickable) {
                            childNodeInfo.getChild(k).performAction(AccessibilityNodeInfo.ACTION_CLICK)//选中图片
                        }
                    }
                }
            }
            val finishList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("完成($picCount/9)")//点击确定
            performClickBtn(finishList)
        }, 2000)
    }


    /**
     * 垂直滑动
     * 滑动比例 0~20
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun slideVertical(startSlideRatio: Int, stopSlideRatio: Int) {
        log("slideVertical")
        val screenHeight = getScreenHeight(this)
        val screenWidth = getScreenWidth(this)
        log("screenHeight $screenHeight")
        log("screenWidth $screenWidth")
        val path = Path()
        val start = screenHeight / 20 * startSlideRatio
        val stop = screenHeight / 20 * stopSlideRatio
        path.moveTo((screenWidth / 2).toFloat(), start.toFloat())//如果只是设置moveTo就是点击
        path.lineTo((screenWidth / 2).toFloat(), stop.toFloat())//如果设置这句就是滑动
        val builder = GestureDescription.Builder()
        val gestureDescription = builder
            .addStroke(
                GestureDescription.StrokeDescription(
                    path,
                    0,
                    500
                )
            )
            .build()

        dispatchGesture(gestureDescription, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                log("onCompleted")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                log("onCancelled")
            }
        }, null)
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    private fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }
}

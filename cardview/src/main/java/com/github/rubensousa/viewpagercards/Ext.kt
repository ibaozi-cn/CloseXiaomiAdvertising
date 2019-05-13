@file:Suppress("DEPRECATION")

package com.github.rubensousa.viewpagercards

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.widget.Toast



/**
 * 前往开启辅助服务界面
 */
fun Context.openAccessSetting() {
    try {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openAppDevlop() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.openApplicationNotify() {
    try {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openBatterySaver() {
    try {
        val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 时间设置
 */
fun Context.openDate() {
    try {
        val intent = Intent(Settings.ACTION_DATE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 打开微信扫一扫
 */
fun Activity.openWechatScan() {
    try {
        val intent = Intent()
        intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
        intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = "android.intent.action.VIEW"
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "请安装微信", Toast.LENGTH_LONG).show()
    }
}

fun Activity.openWechat() {
    val WECHAT_PACKAGE_NAME = "com.tencent.mm"
    val UI_LUANCHER = "$WECHAT_PACKAGE_NAME.ui.LauncherUI"
    try {
        val intent = Intent()
        val cmp = ComponentName(WECHAT_PACKAGE_NAME, UI_LUANCHER)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = cmp
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "请安装微信", Toast.LENGTH_LONG).show()
    }
}

/**
 * 安全中心
 */
fun Context.openSaveCore() {
    val WECHAT_PACKAGE_NAME = "com.miui.securitycenter"
//    val UI_LUANCHER = "com.miui.securityscan.MainPageActivity"
    val UI_SETTING = "com.miui.securityscan.ui.settings.SettingsActivity"
    try {
        val intent = Intent()
        intent.setClassName(WECHAT_PACKAGE_NAME, UI_SETTING)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

/**
 * 垃圾中心
 */
fun Context.openLajiCore() {
    val WECHAT_PACKAGE_NAME = "com.miui.cleanmaster"
    val LAJI = "com.miui.optimizecenter.settings.SettingsActivity"
    try {
        val intent = Intent()
        intent.setClassName(WECHAT_PACKAGE_NAME, LAJI)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

/**
 * 打开应用管理配置
 */
fun Context.openAppManagerSetting() {
    val Package_Name = "com.miui.securitycenter"
//    val Settings = "com.miui.appmanager.AppManagerSettings"
    val Settings = "com.miui.appmanager.AppManagerMainActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openDownSetting() {
    val Package_Name = "com.android.providers.downloads.ui"
//    val Settings = "com.android.providers.downloads.ui.activity.InfoFlowSettingActivity"
//    val Settings = "com.android.providers.downloads.ui.activity.DownloadSettingActivity"
    val Settings = "com.android.providers.downloads.ui.DownloadList"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openAppInstallSetting() {
    val Package_Name = "com.miui.packageinstaller"
    val Settings = "com.android.packageinstaller.SettingsActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openVideoSetting() {
    val Package_Name = "com.miui.video"
    val Settings = "com.miui.video.feature.mine.setting.SettingActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openMusicSetting() {
    val Package_Name = "com.miui.player"
    val Settings = "com.miui.player.phone.ui.MusicSettings"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openCalendarSetting() {
    val Package_Name = "com.android.calendar"
    val Settings = "com.android.calendar.settings.CalendarSettingsActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openWeatherSetting() {
    val Package_Name = "com.miui.weather2"
//    val Settings = "com.miui.weather2.ActivitySet"
    val Settings = "com.miui.weather2.ActivityWeatherMain"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openMarketSetting() {
    val Package_Name = "com.xiaomi.market"
    val Settings = "com.xiaomi.market.ui.MarketPreferenceActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openQuickSearchSetting() {
    val Package_Name = "com.android.quicksearchbox"
    val Settings = "com.android.quicksearchbox.preferences.SearchSettingsPreferenceActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openHomeSetting() {
    val Package_Name = "com.miui.home"
    val Settings = "com.miui.home.settings.MiuiHomeSettingActivity"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.openSogouSetting() {
    val Package_Name = "com.sohu.inputmethod.sogou.xiaomi"
    val Settings = "com.sohu.inputmethod.sogou.xiaomi.SogouIMESettings"
    try {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(Package_Name, Settings)
        startActivity(intent)
    } catch (e: Exception) {
    }
}



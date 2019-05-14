package com.github.rubensousa.viewpagercards

import android.content.Context

/**
 *
 * 项目名称：wechat
 * 类描述：
 * 创建人：zzy(zhanyong.zhang@kanche.com)
 * 创建时间：2019/5/11 12:37 PM
 * 修改人：
 * 修改时间：2019/5/11 12:37 PM
 * 修改备注：
 * @version
 *
 */
object FactoryClose {

    fun closeSet(config: String, context: Context) {
        log(config)
        if (config ==  Action.AutoOpenAppHome.desc) {
            context.openAppHome()
        }
        if (config == Action.AutoOpenAppSettings.desc) {
            context.openAppSetting()
        }
        if (config == Action.AutoOpenSaveCore.desc) {
            context.openSaveCore()
        }
        if (config == Action.AutoOpenLajiCore.desc) {
            context.openLajiCore()
        }
        if (config == "应用管理") {
            context.openAppManagerSetting()
        }
        if (config == "应用市场") {
            context.openMarketSetting()
        }
        if (config == "下载信息") {
            context.openDownSetting()
        }
        if (config == "视频") {
            context.openVideoSetting()
        }
        if (config == "音乐") {
            context.openMusicSetting()
        }
        if (config == "日历") {
            context.openCalendarSetting()
        }
        if (config == "天气") {
            context.openWeatherSetting()
        }
        if (config == "快搜索") {
            context.openQuickSearchSetting()
        }
        if (config == "桌面") {
            context.openHomeSetting()
        }
        if (config == "搜狗") {
            context.openSogouSetting()
        }
        if (config == "开发者") {
            context.openAppDevlop()
        }
    }

}
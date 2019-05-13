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


    fun closeSet(title: String, context: Context) {
        if (title == "安全中心") {
            context.openSaveCore()
        }
        if (title == "垃圾清理") {
            context.openLajiCore()
        }
        if (title == "应用管理") {
            context.openAppManagerSetting()
        }
        if (title == "应用市场") {
            context.openMarketSetting()
        }
        if (title == "下载信息") {
            context.openDownSetting()
        }
        if (title == "视频") {
            context.openVideoSetting()
        }
        if (title == "音乐") {
            context.openMusicSetting()
        }
        if (title == "8日历") {
            context.openCalendarSetting()
        }
        if (title == "天气") {
            context.openWeatherSetting()
        }
        if (title == "快搜索") {
            context.openQuickSearchSetting()
        }
        if (title == "桌面") {
            context.openHomeSetting()
        }
        if (title == "搜狗") {
            context.openSogouSetting()
        }
        if (title == "开发者") {
            context.openAppDevlop()
        }
    }

}
package com.example.julive.wechathelper.util

enum class Action(val desc:String) {
    Nothing("null"), AutoOpenAppHome("回到应用主页"), AutoOpenAppSettings("打开应用设置"), AutoOpenSaveCore("安全中心"), AutoOpenLajiCore("垃圾清理"), AutoOpenAppManagerSetting("应用管理"),
    AutoOpenMarketSetting("应用市场"), AutoOpenDownSetting("下载信息"), AutoOpenVideoSetting("视频"), AutoOpenMusicSetting("音乐"), AutoOpenCalendarSetting("日历"),
    AutoOpenWeatherSetting("天气"),AutoOpenQuickSearchSetting("快搜索"),AutoOpenHomeSetting("桌面"),AutoOpenSogouSetting("搜狗"),AutoOpenAppDevlop("开发者")
}
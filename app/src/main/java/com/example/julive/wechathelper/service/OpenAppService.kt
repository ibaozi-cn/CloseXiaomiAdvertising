package com.example.julive.wechathelper.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.julive.wechathelper.ext.log
import com.example.julive.wechathelper.ext.openWechat
import org.jetbrains.anko.intentFor

class OpenAppService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        openWechat()
        log("OpenAppService", "openWechat==========================")
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        fun startSelf(context: Context) {
            context.startService(context.intentFor<OpenAppService>())
        }
    }

}
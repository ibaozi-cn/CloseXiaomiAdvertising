package com.example.julive.wechathelper

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.topjohnwu.superuser.Shell

class ApplicationXm : Application() {

    private val headSetWatchHandler by lazy {
        HeadSetWatchHandler(handlerThread.looper, applicationContext)
    }

    private val handlerThread by lazy {
        HandlerThread("headSetWatch")
    }

    override fun onCreate() {
        super.onCreate()
        //启动耳机监听线程
        if (!handlerThread.isAlive) {
            handlerThread.start()
            headSetWatchHandler.sendEmptyMessageDelayed(1, 2000)
        }
        Shell.Config.setFlags(Shell.FLAG_REDIRECT_STDERR)
        Shell.Config.verboseLogging(BuildConfig.DEBUG)
        Shell.Config.setTimeout(10)
    }

    override fun onTerminate() {
        super.onTerminate()
        headSetWatchHandler.removeMessages(1)
        handlerThread.quit()
    }

    private class HeadSetWatchHandler internal constructor(looper: Looper, val context: Context) :
        Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    sendEmptyMessageDelayed(1, 1000)


                }
            }
        }
    }


}
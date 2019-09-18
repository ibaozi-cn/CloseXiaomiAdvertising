package com.example.julive.wechathelper.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.julive.wechathelper.ext.log
import com.topjohnwu.superuser.Shell
import org.jetbrains.anko.longToast

class ShellReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val commands = intent?.getStringExtra("commands")
        log("ShellReceiver","ShellReceiver===========================$commands")
        val result = Shell.su(commands).exec()
        if (result.isSuccess) {
            context?.longToast("执行成功")
        } else {
            context?.longToast("执行失败")
        }
    }
}
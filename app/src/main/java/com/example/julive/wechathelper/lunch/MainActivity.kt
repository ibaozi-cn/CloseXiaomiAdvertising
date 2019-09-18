package com.example.julive.wechathelper.lunch

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.example.julive.wechathelper.R
import com.example.julive.wechathelper.ext.ControllerService
import com.example.julive.wechathelper.ext.isAccessibilityServiceEnabled
import com.example.julive.wechathelper.ext.isSuccess
import com.example.julive.wechathelper.ext.startAccessibilityService
import com.example.julive.wechathelper.home.MainHomeActivity
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import org.jetbrains.anko.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        com.example.julive.wechathelper.util.log("onPermissionsDenied" + perms.toString())
        hasPermissions = false
        this.finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        com.example.julive.wechathelper.util.log("onPermissionsGranted" + perms.toString())
        hasPermissions = true
    }

    private var hasPermissions = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!EasyPermissions.hasPermissions(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
        )
            requestPermissions()
        else {
            hasPermissions = true
        }

        Shell.su("cp -r /data/data/com.tencent.mm/MicroMsg /sdcard/cp/").exec()
    }

    fun check(view: View) {
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private fun checkPermission() {
        Handler().postDelayed({
            startActivity(intentFor<MainHomeActivity>())
        }, 3000)
    }


    private fun requestPermissions(): Boolean {
        if (!hasPermissions) {
            EasyPermissions.requestPermissions(
                this,
                "需要读写内存卡权限，请允许才能继续",
                100,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

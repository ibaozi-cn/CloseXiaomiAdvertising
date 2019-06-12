package com.example.julive.wechathelper.lunch

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.example.julive.wechathelper.R
import com.example.julive.wechathelper.home.MainHomeActivity
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
                Manifest.permission.DISABLE_KEYGUARD
            )
        )
            requestPermissions()
        else {
            hasPermissions = true
        }
        val layout = findViewById<LinearLayout>(R.id.layout)
        layout.backgroundResource = R.drawable.bg7
        checkPermission()

//        FileUtil.writeLog(logPath, Action.Nothing.desc, false, "utf-8")
    }

    val bgList = arrayListOf(
        R.drawable.bg1,
        R.drawable.bg2,
        R.drawable.bg3,
        R.drawable.bg4,
        R.drawable.bg5,
        R.drawable.bg6,
        R.drawable.bg7
    )

    override fun onRestart() {
        super.onRestart()
        val layout = findViewById<LinearLayout>(R.id.layout)
        layout.backgroundResource = bgList[Random().nextInt(7)]
    }

    fun check(view: View) {
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
                Manifest.permission.DISABLE_KEYGUARD
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

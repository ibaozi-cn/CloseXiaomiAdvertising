package com.example.julive.wechathelper

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import com.github.rubensousa.viewpagercards.FileUtil
import com.github.rubensousa.viewpagercards.log
import com.github.rubensousa.viewpagercards.logPath
import com.github.rubensousa.viewpagercards.openAccessSetting
import org.jetbrains.anko.alert
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        log("onPermissionsDenied" + perms.toString())
        hasPermissions = false
        this.finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        log("onPermissionsGranted" + perms.toString())
        hasPermissions = true
    }

    private var hasPermissions = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FileUtil.writeLog(logPath, "0", false, "utf-8")
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

    }

    val bgList = arrayListOf(
        R.drawable.bg,
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
        checkPermission()
    }

    private fun checkPermission() {
        if (!isAccessibilityServiceSettingEnabled())
            alert("\n确定: 体验自动化，点完确定后要依次打开：更多已下载的服务->广告剔除助手->开启服务->确定\n\n取消: 自己手动操作", "请求打开无障碍模式") {
                yesButton {
                    FileUtil.writeLog(logPath, "1000", false, "utf-8")
                    openAccessSetting()
                }
                noButton {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    intent.putExtra("isAuto", false)
                    startActivity(intent)
                    this@MainActivity.finish()
                }
            }.show()
        else {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("isAuto", true)
            startActivity(intent)
            this@MainActivity.finish()
        }
    }

    private fun isAccessibilityServiceSettingEnabled(): Boolean {
        val service = packageName + "/" + WechatService::class.java.canonicalName
        val accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
        if (accessibilityEnabled != 1)
            return false
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        val settingValue = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

        mStringColonSplitter.setString(settingValue)
        while (mStringColonSplitter.hasNext()) {
            val accessibilityService = mStringColonSplitter.next()
            if (accessibilityService.equals(service, ignoreCase = true)) {
                return true
            }
        }
        return false
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

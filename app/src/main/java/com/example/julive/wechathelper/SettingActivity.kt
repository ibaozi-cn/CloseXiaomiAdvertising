package com.example.julive.wechathelper

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.widget.ListView
import com.github.rubensousa.viewpagercards.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

var isOpen = false

class SettingActivity : AppCompatActivity() {

    private var isAuto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        isAuto = intent.getBooleanExtra("isAuto", false)

        if (savedInstanceState == null) {
            val fragment = SettingFragment()
            fragmentManager.beginTransaction().add(R.id.fragment, fragment).commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (isAuto)
            toolbar.menu.add("开启").setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        toolbar.menu.add("关于").setShowAsAction(SHOW_AS_ACTION_ALWAYS)

        toolbar.setOnMenuItemClickListener {
            if (it.title == "开启") {
                alert(title = "确认开启自动关闭吗？", message = "确定：点击下面任意一项即可免去手动操作，快去试试吧") {
                    yesButton {
                        isOpen = true
                    }
                    noButton {
                        toast("那你手动吧")
                        isOpen = false
                    }
                }.show()
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentManager.findFragmentById(R.id.fragment)?.let {
            log("divider   ListView")
            it.view?.findViewById<ListView>(android.R.id.list)?.let {
                log("divider   dividerHeight")
                it.divider = null
                it.dividerHeight = 0
            }
        }
    }


    class SettingFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // 加载xml资源文件
            addPreferencesFromResource(R.xml.setting)
        }

        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference): Boolean {
            log("preferenceScreen$preference")
            if (isOpen)
                FileUtil.writeLog(logPath, preference.title.toString(), false, "utf-8")
            else
                FileUtil.writeLog(logPath, Action.Nothing.desc, false, "utf-8")
            FactoryClose.closeSet(preference.title.toString(), activity)
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }
}
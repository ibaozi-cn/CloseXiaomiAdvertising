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
import org.jetbrains.anko.*

private var isAuto = false

class SettingActivity : AppCompatActivity() {

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
            toolbar.menu.add("关闭").setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        toolbar.menu.add("反馈")
        toolbar.menu.add("关于")

        toolbar.setOnMenuItemClickListener { item ->
            if (item.title == "关闭") {
                alert(message = "确认关闭自动吗") {
                    yesButton {
                        isAuto = false
                        longToast("已关闭")
                        item.title = "开启"
                    }

                }.show()
            }
            if (item.title == "开启") {
                alert(message = "确认开启自动吗") {
                    yesButton {
                        isAuto = true
                        longToast("已开启")
                        item.title = "关闭"
                    }
                }.show()
            }
            if (item.title == "反馈") {
                startActivity(intentFor<FeedbackActivity>())
            }
            if (item.title == "关于") {
                startActivity(intentFor<AboutActivity>())
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
            if (isAuto)
                FileUtil.writeLog(logPath, preference.title.toString(), false, "utf-8")
            else
                FileUtil.writeLog(logPath, Action.Nothing.desc, false, "utf-8")
            FactoryClose.closeSet(preference.title.toString(), activity)
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }
}
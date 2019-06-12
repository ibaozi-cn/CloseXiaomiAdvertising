package com.example.julive.wechathelper.home

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ListView
import com.example.julive.wechathelper.about.AboutActivity
import com.example.julive.wechathelper.fb.FeedbackActivity
import com.example.julive.wechathelper.R
import com.example.julive.wechathelper.ext.isAccessibilityServiceSettingEnabled
import com.example.julive.wechathelper.ext.openAccessSetting
import com.example.julive.wechathelper.util.Action
import com.example.julive.wechathelper.util.FactoryClose
import com.example.julive.wechathelper.util.FileUtil
import com.example.julive.wechathelper.util.logPath

import kotlinx.android.synthetic.main.activity_main_home.*
import org.jetbrains.anko.*

private var isAuto = false

class MainHomeActivity : AppCompatActivity() {

    val settingFragment = SettingFragment()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                navigationToSetting()
                if (fragmentManager.findFragmentById(R.id.message) != settingFragment)
                    fragmentManager.beginTransaction().replace(R.id.message, settingFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun navigationToSetting() {
        if (!isAccessibilityServiceSettingEnabled())
            alert("\n确定: 体验自动化，点完确定后要依次打开：更多已下载的服务->广告剔除助手->开启服务->确定\n\n取消: 自己手动操作", "请求打开无障碍模式") {
                yesButton {
                    FileUtil.writeLog(logPath, "1000", false, "utf-8")
                    openAccessSetting()
                }
                noButton {
                    isAuto = false
                    initToolbar(false)
                }
            }.show()
        else {
            isAuto = true
            initToolbar(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initToolbar(false)
    }

    private fun initToolbar(boolean: Boolean) {
        toolbar.menu.clear()
        if (boolean)
            toolbar.menu.add("关闭").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
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
    class SettingFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // 加载xml资源文件
            addPreferencesFromResource(R.xml.setting)
        }

        override fun onResume() {
            super.onResume()
            view?.findViewById<ListView>(android.R.id.list)?.let {
                com.example.julive.wechathelper.util.log("divider   dividerHeight")
                it.divider = null
                it.dividerHeight = 0
            }
        }

        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference): Boolean {
            com.example.julive.wechathelper.util.log("preferenceScreen$preference")
            if (isAuto)
                FileUtil.writeLog(logPath, preference.title.toString(), false, "utf-8")
            else
                FileUtil.writeLog(logPath, Action.Nothing.desc, false, "utf-8")
            FactoryClose.closeSet(preference.title.toString(), activity)
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }
}

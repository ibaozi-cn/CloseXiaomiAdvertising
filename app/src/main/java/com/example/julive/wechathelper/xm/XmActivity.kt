package com.example.julive.wechathelper.xm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.widget.ListView
import com.example.julive.wechathelper.R
import com.example.julive.wechathelper.home.isAuto
import com.example.julive.wechathelper.util.Action
import com.example.julive.wechathelper.util.FactoryClose
import com.example.julive.wechathelper.util.FileUtil
import com.example.julive.wechathelper.util.logPath

class XmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xm)

        if (savedInstanceState == null) {
            val fragment = SettingFragment()
            fragmentManager.beginTransaction().add(R.id.fragment, fragment).commit()
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

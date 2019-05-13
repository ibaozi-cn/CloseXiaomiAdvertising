package com.example.julive.wechathelper

import android.os.Bundle
import android.preference.Preference
import android.support.v7.app.AppCompatActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.widget.ListView
import com.github.rubensousa.viewpagercards.FactoryClose
import com.github.rubensousa.viewpagercards.log


class SettingActivity : AppCompatActivity() {

    private var isAuto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if (savedInstanceState == null) {
            val fragment = SettingFragment()
            fragmentManager.beginTransaction().add(R.id.fragment, fragment).commit()
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
            FactoryClose.closeSet(preference.title.toString(),activity)
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }
}
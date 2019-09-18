package com.example.julive.wechathelper.home

import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.example.julive.wechathelper.about.AboutActivity
import com.example.julive.wechathelper.fb.FeedbackActivity
import com.example.julive.wechathelper.R
import com.example.julive.wechathelper.util.Action
import com.example.julive.wechathelper.util.FactoryClose
import com.example.julive.wechathelper.util.FileUtil
import com.example.julive.wechathelper.util.logPath
import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.activity_main_home.*
import org.jetbrains.anko.*
import android.annotation.SuppressLint
import com.example.julive.wechathelper.ext.*
import com.example.julive.wechathelper.xm.XmActivity


var isAuto = false

class MainHomeActivity : AppCompatActivity(),
    MyFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {

    }
    //    val settingFragment = SettingFragment()
    val notifyFragment = NotifyFragment.newInstance("", "")
    val myFragment = MyFragment.newInstance("", "")
    val xiaoduoFragment = XiaoduoFragment.newInstance("", "")

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentById(R.id.message) != myFragment)
                    supportFragmentManager.beginTransaction().replace(R.id.message, myFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (supportFragmentManager.findFragmentById(R.id.message) != xiaoduoFragment)
                    supportFragmentManager.beginTransaction().replace(R.id.message, xiaoduoFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (supportFragmentManager.findFragmentById(R.id.message) != notifyFragment)
                    supportFragmentManager.beginTransaction().replace(R.id.message, notifyFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initToolbar(false)
        if (supportFragmentManager.findFragmentById(R.id.message) != xiaoduoFragment)
            supportFragmentManager.beginTransaction().replace(R.id.message, xiaoduoFragment).commit()

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

    fun startRedPackagePrint(view: View) {
        if (!isAccessibilityServiceEnabled()) {
            alert(message = "需要您打开无障碍服务开关才可以继续") {
                yesButton {
                    openAccessSetting()
                }
            }.show()
        } else {
            openWechat()
        }
    }

    fun startXmController(view: View){
        val result =
            Shell.su("pm grant com.example.julive.wechathelper android.permission.WRITE_SECURE_SETTINGS")
                .exec()
        if (result.isSuccess) {
            alert("Ai小哆助手可以控制如下功能哦，您只需要对着小米手机的微信发送如下消息：\n小哆亮屏\n小哆解锁\n小哆锁屏\n" +
                    "小哆截屏\n小哆返回\n小哆home\n小哆任务列表\n小哆打开相机\n小哆打开浏览器","温馨提示").show()
            isAccessibilityServiceEnabled(ControllerService).isSuccess({
                toast("控制服务已开启")
            }
                , {
                    toast("控制服务开启中请稍等")
                    startAccessibilityService()
                })
        } else {
            toast("开启失败，您的手机没root或者没有给小哆助手root权限哦")
        }
    }


    fun closeXmAdvertisement(view: View){
        if (!isAccessibilityServiceSettingEnabled())
            alert("\n确定: 体验自动化，点完确定后要依次打开：更多已下载的服务->广告剔除助手->开启服务->确定\n\n取消: 自己手动操作", "请求打开无障碍模式") {
                yesButton {
                    FileUtil.writeLog(logPath, "1000", false, "utf-8")
                    openAccessSetting()
                }
                noButton {
                    isAuto = false
                    initToolbar(false)
                    startActivity(intentFor<XmActivity>())

                }
            }.show()
        else {
            isAuto = true
            initToolbar(true)
            startActivity(intentFor<XmActivity>())
        }
    }


}

package com.example.julive.wechathelper

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.intentFor

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "还没有功能哦", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            val data = Intent(Intent.ACTION_SENDTO)
//            data.data = Uri.parse("zzy0523@gmail.com")
//            data.putExtra(Intent.EXTRA_SUBJECT, "i校长 你好")
//            data.putExtra(Intent.EXTRA_TEXT, "我是在小米广告关闭助手APP内转过来的，有点事情要问你。")
//            startActivity(data)
        }
    }
    fun feedback(view:View){
        startActivity(intentFor<FeedbackActivity>())
    }
}

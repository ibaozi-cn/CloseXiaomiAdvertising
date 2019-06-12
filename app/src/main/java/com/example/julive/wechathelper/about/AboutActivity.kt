package com.example.julive.wechathelper.about

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.julive.wechathelper.fb.FeedbackActivity
import com.example.julive.wechathelper.R
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
        }
    }
    fun feedback(view:View){
        startActivity(intentFor<FeedbackActivity>())
    }
}

package com.example.physicalfitnessexamination.page

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.page.kbi.KbiShuntActivity
import com.example.physicalfitnessexamination.util.toast
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * 首页
 */
class Main2Activity : MyBaseActivity(), View.OnClickListener {

    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, Main2Activity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    override fun initLayout(): Int = R.layout.activity_main2

    override fun initView() {

        tv_new1.setOnClickListener(this)
        tv_new2.setOnClickListener(this)
        tv_new3.setOnClickListener(this)
        btn_main1.setOnClickListener(this)
        btn_main2.setOnClickListener(this)
        btn_main3.setOnClickListener(this)
        btn_main4.setOnClickListener(this)
        btn_main5.setOnClickListener(this)
        iv_LoginOut.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_new1 -> {
                toast("跳转新闻")
            }
            R.id.tv_new2 -> {
                toast("跳转新闻")
            }
            R.id.tv_new3 -> {
                toast("跳转新闻")
            }
            R.id.btn_main1 -> {
                //创建考核
                KbiShuntActivity.startInstant(this)
            }
            R.id.btn_main2 -> {
            }
            R.id.btn_main3 -> {
            }
            R.id.btn_main4 -> {
            }
            R.id.btn_main5 -> {
            }
            R.id.iv_LoginOut -> {
                UserManager.getInstance().loginOut(context);
                finish();
            }
        }
    }
}

package com.example.physicalfitnessexamination.page.rank

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.page.rank.fragment.Rank1Fragment
import com.example.physicalfitnessexamination.page.rank.fragment.Rank2Fragment
import com.example.physicalfitnessexamination.page.rank.fragment.Rank3Fragment
import com.example.physicalfitnessexamination.view.NoScrollViewPager
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.v_toolbar.*

/**
 * 榜上有名 页面
 */
class RankActivity : MyBaseActivity(), View.OnClickListener {
    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, RankActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    /**
     * 标题栏
     */
    private val title = arrayOf("单项成绩记录", "单位历次考核第一", "单位单项团体第一", "消防站总分前五", "个人全能前五")

    /**
     * viewpager
     */
    private val viewPager: NoScrollViewPager by lazy {
        NoScrollViewPager(this)
    }

    override fun initLayout(): Int = R.layout.activity_rank

    override fun initView() {
        tv_title.text = "榜上有名"
        iv_right.setOnClickListener(this)

        viewpager_rank.let { vp ->
            vp.adapter = PageAdapter(supportFragmentManager, title)
            vp.currentItem = 0
            tab_rank.setupWithViewPager(vp)
        }

    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
        }
    }

    class PageAdapter(fm: FragmentManager?, private val titleArray: Array<String>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(i: Int): Fragment {
            return when (i) {
                0 -> Rank1Fragment.newInstance("", "")
                1 -> Rank2Fragment.newInstance("", "")
                2 -> Rank3Fragment.newInstance("", "")
                3 -> Rank3Fragment.newInstance("", "")
                4 -> Rank3Fragment.newInstance("", "")
                else -> Rank3Fragment.newInstance("", "")
            }
        }

        override fun getCount(): Int {
            return titleArray.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleArray.get(position)
        }
    }
}

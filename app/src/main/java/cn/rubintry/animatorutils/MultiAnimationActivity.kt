package cn.rubintry.animatorutils

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.rubintry.animate.AnimateUtils
import cn.rubintry.animate.core.BaseAnimator
import cn.rubintry.animate.core.IAnimatorInterface
import cn.rubintry.animatorutils.adapter.CardAdapter

/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年4月14日
 * 描   述: 多动画同时播放
 * 页面中文名:
 */
class MultiAnimationActivity : AppCompatActivity() {

    private var rvCard: RecyclerView ?= null
    private var btnRefresh: Button ?= null
    private var cardAdapter = CardAdapter(mutableListOf())
    private var tvScale: TextView ?= null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_animation)
        title = "多动画播放"
        rvCard = findViewById(R.id.rv_card)
        btnRefresh = findViewById(R.id.btn_refresh)
        tvScale = findViewById(R.id.tv_scale)
        rvCard?.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        rvCard?.isNestedScrollingEnabled = false
        rvCard?.adapter = cardAdapter

        AnimateUtils.with(tvScale)
            .scale(1.0f , 1.2f , true)
            .enableLoop(true)
            .start()

        cardAdapter.onItemClickListener = object : CardAdapter.OnItemClickListener{
            override fun onItemClick(position: Int , view: View) {

            }

        }

        btnRefresh?.setOnClickListener {
            cardAdapter.cancelNext()
        }

        val dataList = mutableListOf<CardAdapter.Card>(CardAdapter.Card(true , "1") , CardAdapter.Card(true , "2") , CardAdapter.Card(true , "3"))
        cardAdapter.setData(dataList)
    }
}
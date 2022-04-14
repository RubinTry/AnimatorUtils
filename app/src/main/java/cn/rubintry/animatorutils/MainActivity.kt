package cn.rubintry.animatorutils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var tvTranslation: TextView ?= null
    private var tvScale: TextView ?= null
    private var tvMultiAni: TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTranslation = findViewById(R.id.tv_translation)
        tvScale = findViewById(R.id.tv_scale)
        tvMultiAni = findViewById(R.id.tv_multi_ani)

        tvTranslation?.setOnClickListener(this)
        tvScale?.setOnClickListener(this)
        tvMultiAni?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_translation -> {
                startActivity(Intent(this , TranslationActivity::class.java))
            }

            R.id.tv_scale -> {
                startActivity(Intent(this , ScaleActivity::class.java))
            }

            R.id.tv_multi_ani -> {
                startActivity(Intent(this , MultiAnimationActivity::class.java))
            }
        }
    }
}
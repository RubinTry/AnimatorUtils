package cn.rubintry.animatorutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import cn.rubintry.animate.AnimateUtils

class MainActivity : AppCompatActivity() {

    private var tvTest: TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest = findViewById(R.id.tv_test)
        AnimateUtils.with(tvTest)
            .scale(1.0f , 2.0f , true)
            .enableLoop(true)
            .start()

        tvTest?.setOnClickListener {
            if(AnimateUtils.with(tvTest).isAnimateRunning()){
                AnimateUtils.with(tvTest).getAnimator()?.cancel()
            }else{
                AnimateUtils.with(tvTest).getAnimator()?.start()
            }
        }
    }
}
package cn.rubintry.animatorutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import cn.rubintry.animate.AnimateUtils
import cn.rubintry.animate.core.listener.OnAllCompleteListener

class MainActivity : AppCompatActivity() {

    var tvTest: TextView ?= null
    var tvTest2: TextView ?= null
    var tvTest3: TextView ?= null
    var btnStartOrStop: Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest = findViewById(R.id.tv_test)
        tvTest2 = findViewById(R.id.tv_test2)
        tvTest3 = findViewById(R.id.tv_test3)
        btnStartOrStop = findViewById(R.id.btn_start_or_stop)
        AnimateUtils.with(tvTest)
            .scale(1.0f , 2.0f , true)
            .setDuration(400)
            .enableLoop(true)
            .start()

        AnimateUtils.with(tvTest2)
            .translation(0f , 100f , true)
            .setDuration(400)
            .enableLoop(true)
            .start()

        AnimateUtils.with(tvTest3)
            .rotate(90f ,  true)
            .setDuration(400)
            .enableLoop(true)
            .start()


        AnimateUtils.watch(this).waitAllComplete(object : OnAllCompleteListener{
            override fun onAllComplete() {

            }
        })

        var index = 0

        btnStartOrStop?.setOnClickListener {
            if(AnimateUtils.with(tvTest).isAnimateRunning() || AnimateUtils.with(tvTest2).isAnimateRunning() || AnimateUtils.with(tvTest3).isAnimateRunning()){
                when(index){
                    0 -> {
                        AnimateUtils.with(tvTest).cancel()
                    }

                    1 -> {
                        AnimateUtils.with(tvTest2).cancel()
                    }

                    2 -> {
                        AnimateUtils.with(tvTest3).cancel()
                    }
                }
                index ++
            }
        }
    }
}
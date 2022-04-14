package cn.rubintry.animatorutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import cn.rubintry.animate.AnimateUtils

class ScaleActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener,
    View.OnClickListener {

    private var tvMove: TextView?= null
    private var edtScale: EditText?= null
    private var edtScaleDuration: EditText?= null
    private var rdgLoop: RadioGroup?= null
    private var rdgYes: RadioButton?= null
    private var rdgNo: RadioButton?= null
    private var btnStart: Button?= null
    private var needLoop : Boolean ?= null
    private var needReverse: Boolean ?= null
    private var rdgNeedReverse : RadioGroup?= null
    private var rdgReverseYes : RadioButton?= null
    private var rdgReverseNo : RadioButton?= null
    private var btnReset : Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        title = "缩放动画"

        tvMove = findViewById(R.id.tv_move)
        edtScale = findViewById(R.id.edt_scale)
        edtScaleDuration = findViewById(R.id.edt_scale_duration)
        rdgLoop = findViewById(R.id.rdg_loop)
        rdgYes = findViewById(R.id.rdg_yes)
        rdgNo = findViewById(R.id.rdg_no)
        btnStart = findViewById(R.id.btn_start)
        rdgNeedReverse = findViewById(R.id.rdg_need_reverse)
        rdgReverseYes = findViewById(R.id.rdg__reverse_yes)
        rdgReverseNo = findViewById(R.id.rdg__reverse_no)
        btnReset = findViewById(R.id.btn_reset)


        rdgLoop?.setOnCheckedChangeListener(this)
        rdgNeedReverse?.setOnCheckedChangeListener(this)

        btnStart?.setOnClickListener(this)
        btnReset?.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_start -> {
                if(edtScale?.text?.isEmpty() == true){
                    Toast.makeText(this , "请输入缩放倍数", Toast.LENGTH_SHORT).show()
                    return
                }


                if(edtScaleDuration?.text?.isEmpty() == true){
                    Toast.makeText(this , "请输入持续时间", Toast.LENGTH_SHORT).show()
                    return
                }


                val animator = AnimateUtils.with(tvMove)
                    .scale(1f, edtScale?.text?.toString()?.toFloat() ?: 0.0f , needReverse ?: false)
                if(needLoop == true){
                    animator.enableLoop(true)
                }
                animator.setDuration(edtScaleDuration?.text?.toString()?.toLong() ?: 0)
                    .start()
            }

            R.id.btn_reset -> {
                AnimateUtils.with(tvMove).cancel()
                AnimateUtils.with(tvMove).reset()
                edtScale?.setText("")
                edtScaleDuration?.setText("")
                rdgNo?.isChecked = true
                rdgReverseNo?.isChecked = true
            }
        }
    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        if(checkedId == rdgYes?.id){
            needLoop = true
        }
        if(checkedId == rdgReverseYes?.id){
            needReverse = true
        }
    }
}
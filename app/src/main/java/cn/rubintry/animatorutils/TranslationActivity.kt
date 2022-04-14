package cn.rubintry.animatorutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import cn.rubintry.animate.AnimateUtils

class TranslationActivity : AppCompatActivity(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {

    private var tvMove: TextView ?= null
    private var edtOffsetX: EditText ?= null
    private var edtOffsetY: EditText ?= null
    private var edtOffsetDuration: EditText ?= null
    private var rdgLoop: RadioGroup ?= null
    private var rdgYes: RadioButton ?= null
    private var rdgNo: RadioButton ?= null
    private var btnStart: Button ?= null
    private var needLoop : Boolean ?= null
    private var needReverse: Boolean ?= null
    private var rdgNeedReverse : RadioGroup ?= null
    private var rdgReverseYes : RadioButton ?= null
    private var rdgReverseNo : RadioButton ?= null
    private var btnReset : Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)

        tvMove = findViewById(R.id.tv_move)
        edtOffsetX = findViewById(R.id.edt_offset_x)
        edtOffsetY = findViewById(R.id.edt_offset_y)
        edtOffsetDuration = findViewById(R.id.edt_offset_duration)
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
                if(edtOffsetX?.text?.isEmpty() == true){
                    Toast.makeText(this , "请输入x坐标", Toast.LENGTH_SHORT).show()
                    return
                }

                if(edtOffsetY?.text?.isEmpty() == true){
                    Toast.makeText(this , "请输入y坐标", Toast.LENGTH_SHORT).show()
                    return
                }

                if(edtOffsetDuration?.text?.isEmpty() == true){
                    Toast.makeText(this , "请输入持续时间", Toast.LENGTH_SHORT).show()
                    return
                }


                val animator = AnimateUtils.with(tvMove)
                    .translation(edtOffsetX?.text?.toString()?.toFloat()  ?: 0.0f, edtOffsetY?.text?.toString()?.toFloat() ?: 0.0f , needReverse ?: false)
                if(needLoop == true){
                    animator.enableLoop(true)
                }
                animator.setDuration(edtOffsetDuration?.text?.toString()?.toLong() ?: 0)
                    .start()
            }

            R.id.btn_reset -> {
                AnimateUtils.with(tvMove).cancel()
                AnimateUtils.with(tvMove).reset()
                edtOffsetX?.setText("")
                edtOffsetY?.setText("")
                edtOffsetDuration?.setText("")
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
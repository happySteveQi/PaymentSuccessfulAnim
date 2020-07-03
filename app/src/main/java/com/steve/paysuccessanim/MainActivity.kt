package com.steve.paysuccessanim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var animator1:ObjectAnimator
    lateinit var animator2:ObjectAnimator
    val animatorSet = AnimatorSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        initAnim()

        btn_start.setOnClickListener {
            split_anim_view.visibility = View.VISIBLE

            ctv_ok.visibility = View.VISIBLE
            animatorSet.start()

        }
        btn_stop.setOnClickListener {
            ctv_ok.visibility = View.INVISIBLE
            split_anim_view.visibility = View.INVISIBLE
            ctv_ok.stop()

        }
    }

    private fun initAnim(durationTime:Long=800) {
        animator1 = ObjectAnimator.ofFloat(ctv_ok, "scaleX", 0f,1.3f,0.8f)
        animator2 = ObjectAnimator.ofFloat(ctv_ok, "scaleY", 0f,1.3f,0.8f)
        ObjectAnimator.ofFloat()
        animator1.duration = durationTime
        animator2.duration = durationTime
        animatorSet.playTogether(animator1, animator2)
        animatorSet.doOnEnd {
            ctv_ok.start()
        }
    }
}

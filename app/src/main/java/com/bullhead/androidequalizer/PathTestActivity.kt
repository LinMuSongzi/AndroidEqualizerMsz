package com.bullhead.androidequalizer

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bullhead.androidequalizer.waveview.Wave33View


class PathTestActivity : AppCompatActivity() {

    lateinit var view: View
    private var progress = 0f // 当前进度

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_test)

        view = findViewById(R.id.id_lineview)

//        val  waveViewGray =findViewById<Wave33View>(R.id.id_Wave33View);
//
//        val animator = ValueAnimator.ofFloat(0f, 1f)
//        animator.duration = 3000
//        animator.interpolator = LinearInterpolator()
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.addUpdateListener { animation ->
//            progress = animation.animatedValue as Float
//            waveViewGray.setOffset((waveViewGray.width * progress).toInt())
//        }
//        animator.start()

    }

    override fun onResume() {
        super.onResume()
        if (view.tag == null) {
            Log.i("PathTestActivity", "onResume:11111 "+view.height)
//            view.tag = PathTestDrawable(view)
        }
    }
}
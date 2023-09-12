package com.bullhead.androidequalizer

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bullhead.androidequalizer.drawable.PathTestView
import com.bullhead.androidequalizer.waveview.Wave33View


class PathTestActivity : AppCompatActivity() {

    lateinit var view: View
    lateinit var pathTestView: PathTestView
    private var progress = 0f // 当前进度
    var click = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_test)

        view = findViewById(R.id.id_lineview)

        pathTestView = findViewById(R.id.id_lineview)

        Thread {
            while (true) {
                if (click) {
                    Thread.sleep(3000)
                    click = false
                }

                pathTestView.musicDb = (Math.random() * 120).toInt()
            }
        }.start()

    }

    fun lowClick(v: View?) {
        click = true
        pathTestView.musicDb = (Math.random() * 20 + 10).toInt()
    }

    fun midClick(v: View?) {
        click = true
        pathTestView.musicDb = (Math.random() * 30 + 45).toInt()
    }

    fun hightClick(v: View?) {
        click = true
        pathTestView.musicDb = (Math.random() * 30 + 90).toInt()
    }

    override fun onResume() {
        super.onResume()
        if (view.tag == null) {
            Log.i("PathTestActivity", "onResume:11111 " + view.height)
//            view.tag = PathTestDrawable(view)
        }
    }
}
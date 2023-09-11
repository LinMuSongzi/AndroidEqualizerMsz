package com.bullhead.androidequalizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class PathTestActivity : AppCompatActivity() {

    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_test)

        view = findViewById(R.id.id_lineview)


    }

    override fun onResume() {
        super.onResume()
        if (view.tag == null) {
            Log.i("PathTestActivity", "onResume:11111 "+view.height)
//            view.tag = PathTestDrawable(view)
        }
    }
}
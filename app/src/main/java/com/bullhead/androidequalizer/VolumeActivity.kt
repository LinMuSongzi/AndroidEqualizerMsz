package com.bullhead.androidequalizer

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bullhead.androidequalizer.lifecycle.SampleLifcycle
import com.bullhead.androidequalizer.waveview.WaveLineView


class VolumeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val waveLineView = findViewById<View>(R.id.waveLineView) as WaveLineView
                waveLineView.startAnim()
//                waveLineView.setVolume(0)
                val mTextView = findViewById<TextView>(R.id.id_db_tv)
                lifecycle.addObserver(SampleLifcycle(waveLineView) { db ->
                    mTextView.text = "！环境声分贝${db}dB"
                })
            }
        }.launch(Manifest.permission.RECORD_AUDIO)


//根据声音大小进行设置

//根据声音大小进行设置
//        waveLineView.setVolume(db as Int)

    }


}
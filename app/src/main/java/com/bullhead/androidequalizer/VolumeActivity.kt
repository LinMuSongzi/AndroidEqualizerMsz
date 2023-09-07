package com.bullhead.androidequalizer

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bullhead.androidequalizer.lifecycle.SampleLifcycle
import com.bullhead.androidequalizer.waveview.SpectrumView
import com.bullhead.androidequalizer.waveview.WaveLineView
import java.util.Random


class VolumeActivity : AppCompatActivity(), View.OnClickListener {


    val handler = Handler(Looper.getMainLooper())

    var sampleLifecycle : SampleLifcycle? = null
    private lateinit var sv: SpectrumView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val waveLineView = findViewById<View>(R.id.waveLineView) as WaveLineView
                waveLineView.startAnim()
//                waveLineView.setVolume(0)
                val mTextView = findViewById<TextView>(R.id.id_db_tv)
                if(sampleLifecycle == null){
                    sampleLifecycle =  SampleLifcycle(waveLineView) { db ->
                        mTextView.text = "！环境声分贝${db}dB"
                    }
                }
                lifecycle.addObserver(sampleLifecycle!!)
            }
        }.launch(Manifest.permission.RECORD_AUDIO)
        sv = findViewById(R.id.sv);
        findViewById<View>(R.id.btn_start).setOnClickListener(this)
        findViewById<View>(R.id.btn_stop).setOnClickListener(this)
    }

    @Override
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_start -> handler.post(runnable)
            R.id.btn_stop -> handler.removeCallbacks(runnable)
        }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            sv.addSpectrum(Random().nextInt(50))
            handler.postDelayed(this, 100)
        }
    }

    fun lowClick(v:View?){
        sampleLifecycle?.type = 0
    }

    fun midClick(v:View?){
        sampleLifecycle?.type = 1
    }

    fun hightClick(v:View?){
        sampleLifecycle?.type = 2
    }

}
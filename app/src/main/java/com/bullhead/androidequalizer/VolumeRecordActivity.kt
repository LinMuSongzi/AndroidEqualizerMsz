package com.bullhead.androidequalizer

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bullhead.androidequalizer.waveview.BrokenLineView
import com.bullhead.androidequalizer.waveview.WaveLineView
import java.util.LinkedList


class VolumeRecordActivity : AppCompatActivity() {


    private val allVolume = LinkedList<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume_record)
        val view = findViewById<WaveLineView>(R.id.waveLineView)
        view.startAnim()
//        val mGraphicalView = view.execute()
//        mGraphicalView.
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                MediaRecorderDemo(this@VolumeRecordActivity) { db ->
//                    Log.i("VolumeRecordActivity", "MediaRecorderDemo create update: $db")
//
//                    allVolume.add(db)
//                    if (allVolume.size >= view.maxCacheNum) {
//                        allVolume.removeAt(view.maxCacheNum - 1)
//                    }

//                    val a: ArrayList<Double> = ArrayUtil.sub(allVolume, view.maxCacheNum)

                    view.setVolume(db.toInt());

                }.startRecord()
//                lifecycle.addObserver(Volume22Lifecycle(findViewById(R.id.id_VolumeView)))
            }
        }.launch(Manifest.permission.RECORD_AUDIO)

    }
}
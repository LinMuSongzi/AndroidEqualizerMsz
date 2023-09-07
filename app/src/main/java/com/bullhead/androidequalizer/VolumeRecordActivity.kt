package com.bullhead.androidequalizer

import android.Manifest
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bullhead.androidequalizer.lifecycle.VolumeLifecycle

class VolumeRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume_record)

        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                lifecycle.addObserver(VolumeLifecycle(findViewById(R.id.id_VolumeView)))
            }
        }.launch(Manifest.permission.RECORD_AUDIO)

    }
}
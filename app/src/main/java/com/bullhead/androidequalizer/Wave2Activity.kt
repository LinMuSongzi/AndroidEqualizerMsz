package com.bullhead.androidequalizer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bullhead.androidequalizer.lifecycle.Volume22Lifecycle

class Wave2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave2)

//        registerForActivityResult(ActivityResultContracts.RequestPermission()){
//            if(it){
//                lifecycle.addObserver(Volume22Lifecycle(findViewById(R.id.id_VolumeView)))
//            }
//        }.launch(Manifest.permission.RECORD_AUDIO)

    }
}
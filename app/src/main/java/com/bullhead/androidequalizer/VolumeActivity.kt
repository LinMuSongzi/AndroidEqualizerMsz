package com.bullhead.androidequalizer

import android.Manifest
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PermissionResult
import com.bullhead.androidequalizer.lifecycle.VolumeLifecycle


class VolumeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)
        lifecycle.addObserver(VolumeLifecycle(findViewById(R.id.id_volume)))
//        registerForActivityResult(ActivityResultContracts.RequestPermission()){
//            if(it){
//                lifecycle.addObserver(VolumeLifecycle(findViewById(R.id.id_volume)))
//            }
//        }.launch(Manifest.permission.RECORD_AUDIO)

    }


}
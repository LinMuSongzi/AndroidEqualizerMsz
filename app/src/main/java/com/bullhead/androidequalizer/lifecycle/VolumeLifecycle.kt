package com.bullhead.androidequalizer.lifecycle

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class VolumeLifecycle(private val volumeView: VolumeView) : DefaultLifecycleObserver {


    override fun onResume(owner: LifecycleOwner) {
        startRecording(owner)
        owner.lifecycle.removeObserver(this)
    }


    private fun startRecording(owner: LifecycleOwner) {

        volumeView
        Thread {

            while (true) {
                Thread.sleep(50)
                volumeView.upData((Math.random() * 120).toInt())
            }
        }.start()
    }





}
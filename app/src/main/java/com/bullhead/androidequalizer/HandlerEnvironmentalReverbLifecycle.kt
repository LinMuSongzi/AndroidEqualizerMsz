package com.bullhead.androidequalizer

import android.media.audiofx.Virtualizer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class HandlerEnvironmentalReverbLifecycle(private val sessionId: Int) : DefaultLifecycleObserver, Runnable {

    companion object {
        const val TAG = "Virtualizer"
    }

    private lateinit var virtualizer: Virtualizer
    private var value: Short = 0;
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(owner: LifecycleOwner) {
        virtualizer = Virtualizer(0, sessionId)
        virtualizer.enabled = true
        Log.i(TAG, "onCreate: virtualizer.enabled  = "+virtualizer.enabled)
        Log.i(TAG, "onCreate: virtualizer.roundedStrength  = "+virtualizer.roundedStrength)
        Log.i(TAG, "onCreate: virtualizer.properties  = "+virtualizer.properties)
        Log.i(TAG, "onCreate: virtualizer.strengthSupported  = "+virtualizer.strengthSupported)

    }

    override fun onResume(owner: LifecycleOwner) {
        handler.removeCallbacks(this)
        handler.post(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        handler.removeCallbacks(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        virtualizer.release()
    }

    override fun run() {
        Log.i(TAG, "run: value = $value")
        virtualizer.setStrength(value)
        value = value.plus(100).toShort()
        if (value.toInt() >= 1000 + 100) {
            value = 0
        }
        handler.postDelayed(this, 200)
    }


}
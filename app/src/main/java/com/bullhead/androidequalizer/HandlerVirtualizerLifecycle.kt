package com.bullhead.androidequalizer

import android.media.audiofx.Virtualizer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel
import com.bullhead.equalizer.EnableViewModel.Companion.observerEnable
import java.lang.ref.WeakReference

class HandlerVirtualizerLifecycle( val viewModel: EnableViewModel) : DefaultLifecycleObserver, Runnable {

    companion object {
        const val TAG = "Virtualizer"
    }


    private lateinit var virtualizer: Virtualizer
    private var value: Short = 0;
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(owner: LifecycleOwner) {
        viewModel.seesionId.observe(owner){
            it?.apply {
                virtualizer = Virtualizer(0, it)
                virtualizer.enabled = true

                Log.i(TAG, "onCreate: virtualizer2.enabled  = "+virtualizer.enabled)
                Log.i(TAG, "onCreate: virtualizer2.roundedStrength  = "+virtualizer.roundedStrength)
                Log.i(TAG, "onCreate: virtualizer2.properties  = "+virtualizer.properties)
                Log.i(TAG, "onCreate: virtualizer2.strengthSupported  = "+virtualizer.strengthSupported)
                virtualizer.observerEnable(owner,viewModel)
            }
        }

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
        if(this::virtualizer.isInitialized) {
            virtualizer.setStrength(value)
            value = value.plus(100).toShort()
            if (value.toInt() >= 1000 + 100) {
                value = 0
            }
            handler.postDelayed(this, 200)
        }
    }


}
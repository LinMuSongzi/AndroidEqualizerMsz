package com.bullhead.androidequalizer.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bullhead.androidequalizer.waveview.WaveLineView

class SampleLifcycle(val wavVolumeView: WaveLineView, val call: (Int) -> Unit) : DefaultLifecycleObserver {


    var type = 1

    override fun onResume(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        Thread {

            while (owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                Thread.sleep(100)
                var v = 0
                when (type) {
                    0 -> {
                        v = (Math.random() * 30f).toInt()
                    }

                    1 -> {
                        v = (Math.random() * 20f + 30).toInt()
                    }

                    2 -> {
                        v = (Math.random() * 20f + 100).toInt()
                    }
                }

//                if(v % 2 == 0){
//                    v = (Math.random() * 40f+30).toInt()
//                }
                Log.i("VolumeView", "onResume: $v")
                wavVolumeView.setVolume(v)

                wavVolumeView.post {
                    call.invoke(v)

//                    wavVolumeView.invalidate()
                }
            }

        }.start()
    }

}
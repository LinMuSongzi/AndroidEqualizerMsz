package com.bullhead.androidequalizer.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bullhead.androidequalizer.waveview.WaveLineView

class SampleLifcycle(val wavVolumeView: WaveLineView):DefaultLifecycleObserver{


    override fun onResume(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        Thread{

            while (owner.lifecycle.currentState != Lifecycle.State.DESTROYED){
                Thread.sleep(100)
                val v = (Math.random() * 120f).toInt()
                Log.i("VolumeView", "onResume: $v")
                wavVolumeView.setVolume(v)
//                wavVolumeView.post {
//                    wavVolumeView.invalidate()
//                }
            }

        }.start()
    }

}
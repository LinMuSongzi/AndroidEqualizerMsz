package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.EnvironmentalReverb
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel
import com.bullhead.equalizer.EnableViewModel.Companion.observerEnable
import java.lang.Compiler.enable
import java.lang.ref.WeakReference
import java.util.Arrays

class HandlerEnvironmentalReverbLifecycle(viewModel: EnableViewModel) : BaseHandlerAudioEffectLifecycle<EnvironmentalReverb>(viewModel) {



    override fun initRun(mAudioEffect: AudioEffect, owner: LifecycleOwner) {
        if (viewModel.environmentalReverbs.isEmpty()) {
            viewModel.environmentalReverbs.apply {
                add(EnableViewModel.EnvironmentalReverbInfo("setDecayHFRatio", maxProx = 2000, startPox = 100, plusSum = -100))
                add(EnableViewModel.EnvironmentalReverbInfo("setDecayTime", maxProx = 20000, startPox = 100, plusSum = -100))
                add(EnableViewModel.EnvironmentalReverbInfo("setDensity", maxProx = 1000, startPox = 0, plusSum = 0))
                add(EnableViewModel.EnvironmentalReverbInfo("setDiffusion", maxProx = 1000, startPox = 0, plusSum = 0))
                add(EnableViewModel.EnvironmentalReverbInfo("setReflectionsDelay", maxProx = 300, startPox = 0, plusSum = 0))
                add(EnableViewModel.EnvironmentalReverbInfo("setReflectionsLevel", maxProx = 1000, startPox = -9000, plusSum = 9000))
                add(EnableViewModel.EnvironmentalReverbInfo("setReverbDelay", maxProx = 100, startPox = 0, plusSum = 0))
                add(EnableViewModel.EnvironmentalReverbInfo("setReverbLevel", maxProx = 2000, startPox = -9000, plusSum = 9000))
                add(EnableViewModel.EnvironmentalReverbInfo("setRoomHFLevel", maxProx = 0, startPox = -9000, plusSum = 9000))
                add(EnableViewModel.EnvironmentalReverbInfo("setRoomLevel", maxProx = 0, startPox = -9000, plusSum = 9000))
            }
            EnvironmentalReverbDialog().show((owner as FragmentActivity).supportFragmentManager, "asdasd")
        }

        viewModel.environmentalReverbsLivedata.observe(owner) {
            run()
        }
    }

    override fun createAudioEffect(audioId: Int): EnvironmentalReverb  = EnvironmentalReverb(0, audioId)

    override fun run() {
        val reverb = audioEffect ?: return

        Log.d(TAG, "run: environmentalReverbs = ${viewModel.environmentalReverbs.toTypedArray().contentToString()}")

        reverb.decayHFRatio = viewModel.environmentalReverbs[0].thisProx.toShort()
        reverb.decayTime = viewModel.environmentalReverbs[1].thisProx
        reverb.density = viewModel.environmentalReverbs[2].thisProx.toShort()
        reverb.diffusion = viewModel.environmentalReverbs[3].thisProx.toShort()
        reverb.reflectionsDelay = viewModel.environmentalReverbs[4].thisProx
        reverb.reflectionsLevel = viewModel.environmentalReverbs[5].thisProx.toShort()
        reverb.reverbDelay = viewModel.environmentalReverbs[6].thisProx
        reverb.reverbLevel = viewModel.environmentalReverbs[7].thisProx.toShort()
        reverb.roomHFLevel = viewModel.environmentalReverbs[8].thisProx.toShort()
        reverb.roomLevel = viewModel.environmentalReverbs[9].thisProx.toShort()
    }
}
package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.PresetReverb
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel

class PresetReverbLifecycle(viewModel: EnableViewModel) : BaseHandlerAudioEffectLifecycle<PresetReverb>(viewModel) {




    override fun initRun(mAudioEffect: AudioEffect, owner: LifecycleOwner) {

    }

    override fun createAudioEffect(audioId: Int): PresetReverb {
        return PresetReverb(0, audioId)
    }

    override fun run() {
        audioEffect?.preset = PresetReverb.PRESET_LARGEROOM;
    }
}
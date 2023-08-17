package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.PresetReverb
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel

class PresetReverbLifecycle(viewModel: EnableViewModel) : BaseHandlerAudioEffectLifecycle<PresetReverb>(viewModel) {
    companion object {
        val PRESET_NONE: Short = 0

        /**
         * Reverb preset representing a small room less than five meters in length
         */
        val PRESET_SMALLROOM: Short = 1

        /**
         * Reverb preset representing a medium room with a length of ten meters or less
         */
        val PRESET_MEDIUMROOM: Short = 2

        /**
         * Reverb preset representing a large-sized room suitable for live performances
         */
        val PRESET_LARGEROOM: Short = 3

        /**
         * Reverb preset representing a medium-sized hall
         */
        val PRESET_MEDIUMHALL: Short = 4

        /**
         * Reverb preset representing a large-sized hall suitable for a full orchestra
         */
        val PRESET_LARGEHALL: Short = 5

        /**
         * Reverb preset representing a synthesis of the traditional plate reverb
         */
        val PRESET_PLATE: Short = 6


    }

    val arrays = shortArrayOf(PRESET_NONE, PRESET_SMALLROOM, PRESET_MEDIUMROOM, PRESET_LARGEROOM, PRESET_MEDIUMHALL, PRESET_LARGEHALL, PRESET_PLATE)

    override fun initRun(mAudioEffect: AudioEffect, owner: LifecycleOwner) {
        run()
    }

    override fun createAudioEffect(audioId: Int): PresetReverb {
        return PresetReverb(0, audioId)
    }

    override fun run() {
//        audioEffect?.preset = arrays[(Math.random() * arrays.size).toInt()].apply {
//            Log.d(TAG, "run:      audioEffect?.preset  = $this ")
//        }
        audioEffect?.preset  = PRESET_LARGEHALL
//        handler.postDelayed(this, 2000)
    }
}
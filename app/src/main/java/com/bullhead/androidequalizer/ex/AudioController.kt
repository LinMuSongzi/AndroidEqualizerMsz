package com.bullhead.androidequalizer.ex

import android.media.MediaFormat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class AudioController(viewModel: MediaDatasViewModel) :MediaController(viewModel) {

    override fun observerMediaFormat(source: LifecycleOwner, s: Observer<MediaFormat>) {
        viewModel.observerAudioFormat(source,s)
    }

    override fun onChanged(t: MediaFormat?) {

    }

}
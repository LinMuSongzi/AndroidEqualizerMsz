package com.bullhead.androidequalizer.ex

import android.media.MediaFormat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class VideoController(viewModel: MediaDatasViewModel) : MediaController(viewModel) {

    override fun observerMediaFormat(source: LifecycleOwner, s: Observer<MediaFormat>) {
        viewModel.observerVideoFormat(source, s)
    }

    override fun onChanged(t: MediaFormat?) {

    }

}
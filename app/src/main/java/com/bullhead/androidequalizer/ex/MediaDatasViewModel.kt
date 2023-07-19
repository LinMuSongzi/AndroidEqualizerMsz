package com.bullhead.androidequalizer.ex

import android.media.MediaFormat
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

interface MediaDatasViewModel {

    var uri:String

    fun observerAudioFormat(l:LifecycleOwner,s:Observer<MediaFormat>)

    fun observerVideoFormat(l:LifecycleOwner,s:Observer<MediaFormat>)
}
package com.bullhead.androidequalizer.ex

import android.media.MediaCodec
import android.media.MediaFormat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

abstract class MediaController(protected val viewModel: MediaDatasViewModel) : IController, LifecycleEventObserver, Observer<MediaFormat> {

//    private val getObserver:Observer<MediaFormat>
//        get() {
//            return this
//        }

    private lateinit var mediaCodec: MediaCodec

    fun getMediaCode(): MediaCodec? {
        return if (!this::mediaCodec.isInitialized) {
            null
        } else {
            mediaCodec
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                onCreate(source)
            }

            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_RESUME -> {

            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_STOP -> {

            }

            Lifecycle.Event.ON_DESTROY -> {

            }

            Lifecycle.Event.ON_ANY -> {

            }
        }
    }

    private fun onCreate(source: LifecycleOwner) {
        observerMediaFormat(source, this)
    }

    abstract fun observerMediaFormat(source: LifecycleOwner, s: Observer<MediaFormat>)


    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

}
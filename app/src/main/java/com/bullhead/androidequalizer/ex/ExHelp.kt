package com.bullhead.androidequalizer.ex

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class ExHelp private constructor() : LifecycleEventObserver, IController, IHolderMedia {

    lateinit var controllers: Array<MediaController>
//    var defualt = false


    companion object {

        const val AUIDO_INDEX = 0
        const val VIDEO_INDEX = 1

        fun LifecycleOwner.createExHelp(): IController = ExHelp().apply {
            lifecycle.addObserver(this)
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
//        controllers = arrayOf(AudioController(), VideoController())
        source.lifecycle.addObserver(getAudioController())
        source.lifecycle.addObserver(getVideoController())
    }

    override fun start() {
        getAudioController().start()
        getVideoController().start()
    }

    override fun pause() {
        getAudioController().pause()
        getVideoController().pause()
    }

    override fun stop() {
        getAudioController().stop()
        getVideoController().stop()
    }

    override fun getAudioController(): AudioController = controllers[AUIDO_INDEX] as AudioController

    override fun getVideoController(): VideoController = controllers[VIDEO_INDEX] as VideoController
}
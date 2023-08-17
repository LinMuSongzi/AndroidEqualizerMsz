package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.Virtualizer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel
import com.bullhead.equalizer.EnableViewModel.Companion.observerEnable

abstract class BaseHandlerAudioEffectLifecycle<T:AudioEffect>(protected val viewModel: EnableViewModel): DefaultLifecycleObserver, Runnable {

    companion object {
        const val TAG = "AudioEffectLifecycle"
    }

    protected val handler = Handler(Looper.getMainLooper())
    private var mAudioEffect:T? = null

    val  audioEffect:T?
        get() {
            return mAudioEffect
        }

    override fun onCreate(owner: LifecycleOwner) {
        viewModel.seesionId[0].observe(owner) {
            it?.apply {
//                if (AudioEffect.isEffectTypeAvailable(UUID.fromString(Virtualizer.EFFECT_TYPE_VIRTUALIZER))) {
                if (mAudioEffect != null) {
                    mAudioEffect!!.release()
                }
                mAudioEffect = createAudioEffect(this)//Virtualizer(1, it)

//                mAudioEffect?.observerEnable(owner, viewModel)

                initRun(mAudioEffect!!,owner)
                mAudioEffect?.enabled = true
                Log.i(TAG, "onCreate: BaseHandlerAudioEffectLifecycle.enabled  = " + mAudioEffect!!.enabled)
            }
        }
    }

    abstract fun initRun(mAudioEffect: AudioEffect,owner: LifecycleOwner)


    abstract fun createAudioEffect(audioId:Int):T

    override fun onStop(owner: LifecycleOwner) {
        handler.removeCallbacks(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mAudioEffect?.release()
    }
}
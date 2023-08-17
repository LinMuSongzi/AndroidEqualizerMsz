package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.Virtualizer
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel

class HandlerVirtualizerLifecycle(viewModel: EnableViewModel) : BaseHandlerAudioEffectLifecycle<Virtualizer>(viewModel) {

    var lenght: Short = 1000;


    override fun initRun(mAudioEffect: AudioEffect, owner: LifecycleOwner) {
        run()
    }

    override fun createAudioEffect(audioId: Int) = Virtualizer(0, audioId)

    override fun run() {
        val mVirtualizer = audioEffect ?: return

        Log.d(TAG, "run: mVirtualizer = $mVirtualizer , lenght = $lenght , isEnable = ${mVirtualizer.enabled}")

        // 设置强度级别
        mVirtualizer.setStrength(lenght) // 在0到1000之间，以毫贝为单位

        lenght = (Math.random() * 1000).toInt().toShort()
//        mVirtualizer!!.enabled = true
        handler.postDelayed(this, 1000)

//        mVirtualizer.(1.0f)

    }


}
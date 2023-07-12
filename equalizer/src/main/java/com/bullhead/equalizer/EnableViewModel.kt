package com.bullhead.equalizer

import android.media.audiofx.AudioEffect
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EnableViewModel : ViewModel() {


    val seesionId = MutableLiveData<Int>()

    val enable = MutableLiveData<Boolean>()


    companion object {


        fun <T:AudioEffect> T.observerEnable(lifecycleOwner: LifecycleOwner, vm: EnableViewModel) {
            vm.enable.observe(lifecycleOwner) {
                it?.apply {
                    enabled = it
                }
            }
        }
    }

}
package com.bullhead.androidequalizer

import android.media.audiofx.EnvironmentalReverb
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel
import com.bullhead.equalizer.EnableViewModel.Companion.observerEnable
import java.lang.Compiler.enable
import java.lang.ref.WeakReference

class HandlerEnvironmentalReverbLifecycle(val viewModel: EnableViewModel) : DefaultLifecycleObserver, Runnable {

    companion object {
        const val TAG = "EnvironmentalReverb"

//        const val
    }

    init {
        Log.i(TAG, ": viewModel = $viewModel")
    }

//    private val viewModel = WeakReference(vm)


    private var open: Int = 3
    private var reverb: EnvironmentalReverb? = null
    private val handler = Handler(Looper.getMainLooper())

//    private  var roomLevel = 0


    override fun onCreate(owner: LifecycleOwner) {
        viewModel.seesionId.observe(owner) {
            it?.apply {
                if (reverb != null) {
                    reverb!!.release()
                }
                reverb = EnvironmentalReverb(0, it)
                reverb!!.enabled = true
                reverb?.observerEnable(owner,viewModel)
                Log.i(TAG, "onCreate: EnvironmentalReverb.enabled  = " + reverb!!.enabled)
                Log.i(TAG, "onCreate: EnvironmentalReverb.properties  = " + reverb!!.properties)
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        handler.removeCallbacks(this)
        run()
    }

    override fun onStop(owner: LifecycleOwner) {
        handler.removeCallbacks(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        reverb?.release()
    }

    override fun run() {
        val reverb = reverb ?: return
        Log.i(TAG, "run: open = $open")
        when (open) {
            1 -> {
                reverb.setRoomLevel(-500); // 房间级别设置为-500mB
                reverb.setRoomHFLevel(-500); // 房间高频级别设置为-500mB
                reverb.setDecayTime(2000); // 衰减时间设置为2000ms
                reverb.setDecayHFRatio(1000); // 衰减高频比率设置为1000
                reverb.setReflectionsLevel(-1000) // 反射级别设置为-1000mB
                reverb.setReflectionsDelay(20); // 反射延迟设置为20ms
                reverb.setReverbLevel(1000); // 混响级别设置为1000mB
                reverb.setReverbDelay(100); // 混响延迟设置为100ms
                reverb.setDiffusion(1000); // 扩散度设置为1000
                reverb.setDensity(1000); // 密度设置为1000
                reverb.setEnabled(true); // 启用混响效果
            }

            2 -> {
                reverb.setRoomLevel(-400); // 房间级别设置为-400mB
                reverb.setRoomHFLevel(-200); // 房间高频级别设置为-200mB
                reverb.setDecayTime(1500); // 衰减时间设置为1500ms
                reverb.setDecayHFRatio(700); // 衰减高频比率设置为700
                reverb.setReflectionsLevel(-900); // 反射级别设置为-900mB
                reverb.setReflectionsDelay(20); // 反射延迟设置为20ms
                reverb.setReverbLevel(500); // 混响级别设置为500mB
                reverb.setReverbDelay(50); // 混响延迟设置为50ms
                reverb.setDiffusion(500); // 扩散度设置为500
                reverb.setDensity(500); // 密度设置为500
                reverb.setEnabled(true); // 启用混响效果
            }

            3 -> {
                reverb.setRoomLevel(-0); // 房间级别设置为-600mB
                reverb.setRoomHFLevel(-0); // 房间高频级别设置为-100mB
                reverb.setDecayTime(0); // 衰减时间设置为7000ms
                reverb.setDecayHFRatio(0); // 衰减高频比率设置为2100
                reverb.setReflectionsLevel(-0); // 反射级别设置为-2000mB
                reverb.setReflectionsDelay(0); // 反射延迟设置为40ms
                reverb.setReverbLevel(0); // 混响级别设置为1300mB
                reverb.setReverbDelay(0); // 混响延迟设置为100ms
                reverb.setDiffusion(0); // 扩散度设置为1000
                reverb.setDensity(0); // 密度设置为700
                reverb.setEnabled(true); // 启用混响效果
            }
        }
//        open++
//        if (open % 3 == 0) {
//            open = 0
//        }
//        handler.postDelayed(this, 5000)
    }


}
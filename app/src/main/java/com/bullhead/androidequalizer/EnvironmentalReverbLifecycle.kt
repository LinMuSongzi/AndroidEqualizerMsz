package com.bullhead.androidequalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.EnvironmentalReverb
import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.bullhead.equalizer.EnableViewModel

class EnvironmentalReverbLifecycle(viewModel: EnableViewModel) : BaseHandlerAudioEffectLifecycle<EnvironmentalReverb>(viewModel) {


    override fun initRun(mAudioEffect: AudioEffect, owner: LifecycleOwner) {
        if (viewModel.environmentalReverbs.isEmpty()) {
            viewModel.environmentalReverbs.apply {
                add(EnableViewModel.EnvironmentalReverbInfo("setDecayHFRatio", maxProx = 2000, startPox = 100, plusSum = -100, position = 0,tag = "无量纲的比率",detail = "这是混响的高频衰减比率").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()

                        Log.d(TAG, "setDecayHFRatio: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.decayHFRatio = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setDecayTime", maxProx = 20000, startPox = 100, plusSum = -100, position = 1,tag = "毫秒", detail = "这是混响的衰减时间").apply {
                    callMethod = {
                        val value = (thisProx - plusSum)
                        Log.d(TAG, "setDecayTime: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.decayTime = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setDensity", maxProx = 1000, startPox = 0, plusSum = 0, position = 2,tag = "百分比", detail = "这是混响的密度").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()
                        Log.d(TAG, "setDensity: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.density = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setDiffusion", maxProx = 1000, startPox = 0, plusSum = 0, position = 3, tag = "百分比", detail = "是混响的扩散程度").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()
                        Log.d(TAG, "setDiffusion: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.diffusion = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setReflectionsDelay", maxProx = 300, startPox = 0, plusSum = 0, position = 4 ,tag = "毫秒", detail = "这是首次反射声音的延迟时间").apply {
                    callMethod = {
                        val value = (thisProx - plusSum)
                        Log.d(TAG, "setReflectionsDelay: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.reflectionsDelay = value
                    }
                })
                add(
                    EnableViewModel.EnvironmentalReverbInfo("setReflectionsLevel", maxProx = 1000, startPox = -9000, plusSum = 9000, position = 5,tag = "毫升贝", detail = "这是首次反射声音的衰减")
                        .apply {
                            callMethod = {
                                val value = (thisProx - plusSum).toShort()
                                Log.d(TAG, "setReflectionsLevel: max = $maxProx , startPro = $startPox , value =  $value")
                                audioEffect?.reflectionsLevel = value
                            }
                        })
                add(EnableViewModel.EnvironmentalReverbInfo("setReverbDelay", maxProx = 100, startPox = 0, plusSum = 0, position = 6 , tag="毫秒", detail = "这是后续混响声音的延迟时间").apply {
                    callMethod = {
                        val value = (thisProx - plusSum)
                        Log.d(TAG, "setReverbDelay: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.reverbDelay = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setReverbLevel", maxProx = 2000, startPox = -9000, plusSum = 9000, position = 7,tag = "毫升贝", detail = "这是后续混响声音的衰减").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()
                        Log.d(TAG, "setReverbLevel: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.reverbLevel = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setRoomHFLevel", maxProx = 0, startPox = -9000, plusSum = 9000, position = 8 ,tag = "毫升贝", detail = "这是在混响环境中高频部分的衰减").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()
                        Log.d(TAG, "setRoomHFLevel: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.roomHFLevel = value
                    }
                })
                add(EnableViewModel.EnvironmentalReverbInfo("setRoomLevel", maxProx = 0, startPox = -9000, plusSum = 9000, position = 9,tag = "毫升贝", detail = "这是混响效果的环境大小").apply {
                    callMethod = {
                        val value = (thisProx - plusSum).toShort()
                        Log.d(TAG, "setRoomLevel: max = $maxProx , startPro = $startPox , value =  $value")
                        audioEffect?.roomLevel = value
                    }
                })
            }
            Log.d(TAG, "audioEffect properties: ${audioEffect?.properties}")
            EnvironmentalReverbDialog().show((owner as FragmentActivity).supportFragmentManager, "asdasd")
        }

        viewModel.currentEnvironmentalReverbInfo.observe(owner) {
            it.callMethod?.invoke(audioEffect)

//            run()
        }
    }

    override fun createAudioEffect(audioId: Int): EnvironmentalReverb = EnvironmentalReverb(0, audioId)

    override fun run() {
        val reverb = audioEffect ?: return

//        Log.d(TAG, "run: environmentalReverbs = ${viewModel.environmentalReverbs.toTypedArray().contentToString()}")

//        reverb.decayHFRatio = viewModel.environmentalReverbs[0].thisProx.toShort()
//        reverb.decayTime = viewModel.environmentalReverbs[1].thisProx
//        reverb.density = viewModel.environmentalReverbs[2].thisProx.toShort()
//        reverb.diffusion = viewModel.environmentalReverbs[3].thisProx.toShort()
//        reverb.reflectionsDelay = viewModel.environmentalReverbs[4].thisProx
//        reverb.reflectionsLevel = viewModel.environmentalReverbs[5].thisProx.toShort()
//        reverb.reverbDelay = viewModel.environmentalReverbs[6].thisProx
//        reverb.reverbLevel = viewModel.environmentalReverbs[7].thisProx.toShort()
//        reverb.roomHFLevel = viewModel.environmentalReverbs[8].thisProx.toShort()
//        reverb.roomLevel = viewModel.environmentalReverbs[9].thisProx.toShort()
    }
}
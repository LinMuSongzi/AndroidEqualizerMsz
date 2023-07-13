package com.bullhead.equalizer

import android.content.Context
import android.media.audiofx.AudioEffect
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.audio.DefaultAudioSink

class EnableViewModel : ViewModel() {


    val seesionId = Array(3) {
        MutableLiveData<Int>()
    }

    val enable = MutableLiveData<Boolean>()


    companion object {


        @JvmStatic
        @JvmOverloads
        fun exoPlaySImple(
            context: Context,
            lifecycleOwner: LifecycleOwner,
            //请填写本地/网络的 wav文件
            paths: String,
            isPlay: Boolean? = false
        ): ExoPlayer {

            //1. 创建播放器
            val player = ExoPlayer.Builder(context).setRenderersFactory(object :
                DefaultRenderersFactory(context) {
                override fun buildAudioSink(
                    context: Context,
                    enableFloatOutput: Boolean,
                    enableAudioTrackPlaybackParams: Boolean,
                    enableOffload: Boolean
                ): AudioSink? {
                    return DefaultAudioSink.Builder()
                        .setAudioCapabilities(AudioCapabilities.getCapabilities(context))
                        .setEnableFloatOutput(enableFloatOutput)
                        .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                        /**
                         * 设置SoxAudioProcessor
                         * 处理音频数据
                         */
                        //.setAudioProcessors(arrayOf(audioProcessor))
                        .setOffloadMode(
                            if (enableOffload) DefaultAudioSink.OFFLOAD_MODE_ENABLED_GAPLESS_REQUIRED else DefaultAudioSink.OFFLOAD_MODE_DISABLED
                        )
                        .build()
                }
            }).build()

//            playEffect(player)


            player.repeatMode = Player.REPEAT_MODE_ONE
            player.setMediaItem(MediaItem.fromUri(paths))
//        }

            //4.当Player处于STATE_READY状态时，进行播放
            player.playWhenReady = true

            //5. 调用prepare开始加载准备数据，该方法时异步方法，不会阻塞ui线程
            player.prepare()
            if (isPlay == true) {
                player.play() //  此时处于 STATE_BUFFERING = 2;
            }
//            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
//                override fun onDestroy(owner: LifecycleOwner) {
//                    player.stop()
//                    player.release()
//                }
//            })

            return player;

        }

        fun <T : AudioEffect> T.observerEnable(lifecycleOwner: LifecycleOwner, vm: EnableViewModel) {
            vm.enable.observe(lifecycleOwner) {
                it?.apply {
                    enabled = it
                }
            }
        }
    }

}
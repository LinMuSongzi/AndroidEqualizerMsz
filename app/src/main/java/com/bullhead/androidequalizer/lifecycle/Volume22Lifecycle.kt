package com.bullhead.androidequalizer.lifecycle

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bullhead.androidequalizer.SpectMainActivity
import com.bullhead.androidequalizer.waveview.BrokenLineView
import com.bullhead.androidequalizer.waveview.Spectrogram
import com.bullhead.androidequalizer.waveview.VoiceLineView
import kotlin.math.abs
import kotlin.math.log10

class Volume22Lifecycle(private val spectrogram: BrokenLineView) : DefaultLifecycleObserver {


    companion object {
        const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO // 单声道
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val SAMPLE_RATE = 44100
    }

//    init {
//        spectrogram.setBitspersample(SAMPLE_RATE)
//    }

//    private fun startRecording(owner: LifecycleOwner) {
//
//        volumeView
//        Thread {
//
//            while (true) {
//                Thread.sleep(20)
//                volumeView.setVolume((Math.random() * 100).toInt())
//                volumeView.post {
//                    volumeView.invalidate()
//                }
//            }
//        }.start()
//    }


    private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
    private var audioRecord: AudioRecord? = null
    private var isRecording = false


//    override fun onCreate(owner: LifecycleOwner) {
//
//    }

    override fun onResume(owner: LifecycleOwner) {
        if (audioRecord == null) {
            startRecording(owner)
        }
    }


    private fun startRecording(owner: LifecycleOwner) {
        if (ActivityCompat.checkSelfPermission(owner as Context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE)
        audioRecord!!.startRecording()
        isRecording = true
        Thread {
            val buffer = ShortArray(Spectrogram.SAMPLING_TOTAL)
            Log.i("AudioRecord", "startRecording: 最小缓冲区 = ${Spectrogram.SAMPLING_TOTAL}")
            var bytesRead = 0

            var a: Long
            var T: Long
            var buf: IntArray
            var offset = 0

            while (isRecording) {
                bytesRead = audioRecord!!.read(buffer, 0, Spectrogram.SAMPLING_TOTAL)
                // 在这里处理PCM数据
                // buffer中的数据就是音频的PCM数据
//                spectrogram.updateDate()
//                while (offset < bytesRead - Spectrogram.SAMPLING_TOTAL) {
//                    T = System.nanoTime() / 1000000
//                    buf = IntArray(bytesRead)
//                    for (i in 0 until bytesRead) {
//                        buf[i] = buffer[i].toInt()
//                    }

//                spectrogram.setVolume((Math.random() * 30+30).toInt())

//                Log.i("startRecording", "startRecording: buf size = "+buf.size+" , bytesRead = $bytesRead")
//                    spectrogram.post {
//                        spectrogram.ShowSpectrogram(buf,false, SAMPLE_RATE.toDouble())
//                    }

//                    offset += Spectrogram.SAMPLING_TOTAL * 10 / 17
//                    while (true) {
//                        a = System.nanoTime() / 1000000
//                        if (a - T >= 100) break
//                    }


//                }

            }
        }.start()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        stopRecording()
    }

    private fun stopRecording() {
        isRecording = false
        audioRecord!!.stop()
        audioRecord!!.release()
    }

    /**
     * Convert a PCM value to dB.
     *
     * @param pcm value to convert
     * @return value in dB
     */
    fun pcmToDb(pcm: Number): Float {
        return 20 * log10((abs(pcm.toFloat()) / 32768) / 20e-6f)   // This value is not calibrated
    }

    /**
     * Resample the array to 60Hz (from [MeterService.SAMPLE_RATE]).
     *
     * @param originalArray array to resample
     * @return resampled array
     */
    private fun downsampleTo60Hz(originalArray: FloatArray): FloatArray {
        val originalSampleRate = SAMPLE_RATE
        val targetSampleRate = 60
        val downsampleFactor = originalSampleRate / targetSampleRate
        val downsampledArraySize = originalArray.size / downsampleFactor
        val downsampledArray = FloatArray(downsampledArraySize)
//        Log.d(TAG, "downsampleTo60Hz: originalArray size: ${originalArray.size} downsampledArray size: ${downsampledArray.size}")

        for (i in 0 until downsampledArraySize) {
            val originalIndex = (i * downsampleFactor)
            downsampledArray[i] = originalArray[originalIndex]
        }

        return downsampledArray
    }

}
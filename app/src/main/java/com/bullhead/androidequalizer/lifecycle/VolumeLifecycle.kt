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
import com.bullhead.androidequalizer.waveview.WaveLineView
import kotlin.math.abs
import kotlin.math.log10

class VolumeLifecycle(private val volumeView: WaveLineView) : DefaultLifecycleObserver {


    private val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO // 单声道
    private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    private val SAMPLE_RATE = 44100
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
            val buffer = ShortArray(BUFFER_SIZE)
            Log.i("AudioRecord", "startRecording: 最小缓冲区 = $BUFFER_SIZE")
            var bytesRead = 0
            while (isRecording) {
                bytesRead = audioRecord!!.read(buffer, 0, BUFFER_SIZE)
                // 在这里处理PCM数据
                // buffer中的数据就是音频的PCM数据

                val left = buffer.slice(0 until bytesRead step 2).map { pcmToDb(it) }.toFloatArray()
                val right = buffer.slice(1 until bytesRead step 2).map { pcmToDb(it) }.toFloatArray()

                // downsample to 60Hz (from 44100Hz) and take only the read part of the array
                val leftValues = downsampleTo60Hz(left.sliceArray(0 until bytesRead / 2)).filter { it != Float.NEGATIVE_INFINITY }
//                leftQueue.addAll(leftValues)
//                val rightValues = downsampleTo60Hz(right.sliceArray(0 until bytesRead / 2)).filter { it != Float.NEGATIVE_INFINITY }
//                rightQueue.addAll(rightValues)


                volumeView.post {
                    for(value in leftValues) {
                        Log.i("volumeView", "startRecording: db = $value")
                        volumeView.setVolume(value.toInt())
                        volumeView.invalidate()
                    }
                }
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
    fun pcmToDb(pcm: Number) : Float {
        return 20 * log10( (abs(pcm.toFloat()) /32768) / 20e-6f)   // This value is not calibrated
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
package com.bullhead.equalizer

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.audio.BaseAudioProcessor
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class LoadAudioProcessor : BaseAudioProcessor() {

    private var fileOuputStream: FileOutputStream? = null

    val TAG = "LoadAudioProcessor"

    override fun onConfigure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        AudioProcessor::class.java.classLoader.loadClass("android.app.ActivityThread")?.getDeclaredMethod("currentApplication")?.invoke(null)?.apply {
            fileOuputStream = File((this as Context).dataDir, "qu_test.pcm").let {
                if (it.exists()) {
                    it.delete()
                    Log.i(TAG, "onConfigure: 2")
                } else {
                    it.createNewFile()
                    Log.i(TAG, "onConfigure: 1")
                }
                FileOutputStream(it, true)
            }
        }
        return inputAudioFormat
    }


    override fun queueInput(inputBuffer: ByteBuffer) {
        Log.i(TAG, "queueInput: 1 "+inputBuffer.remaining())
        if (!inputBuffer.hasRemaining()) {
            return
        }
        val remaining = inputBuffer.remaining()
        val byteArray = ByteArray(inputBuffer.remaining())
        inputBuffer.get(byteArray)
        fileOuputStream?.write(byteArray)
        Log.i(TAG, "queueInput: 2 "+byteArray.size)
        replaceOutputBuffer(remaining).flip()
    }

    override fun onFlush() {
        super.onFlush()
        fileOuputStream?.flush()
    }

    override fun onQueueEndOfStream() {
        super.onQueueEndOfStream()
        Log.i(TAG, "onQueueEndOfStream: ")
        fileOuputStream?.flush()
        fileOuputStream?.close()
        fileOuputStream = null
    }

}
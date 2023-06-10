package com.example.languagelearningrecorder

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AudioRecorderClass(private val cacheDir: File) {

    private var myMediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var lastRecordedFilePath: String? = null

    fun getCurrentAudioLevel(): Int {
        val level = myMediaRecorder?.maxAmplitude
        val editedLevel = level?.div(2) ?: 0
        return editedLevel

    }
    fun startRecording() {
        if (isRecording) return

        val outputFile = createOutputFile()
        lastRecordedFilePath = outputFile.absolutePath



        myMediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)

            try {
                prepare()
                start()
                isRecording = true
            } catch (error: IOException) {
                error.printStackTrace()
            }
        }
    }

    private fun createOutputFile(): File {
        val fileName = "my_audio_${System.currentTimeMillis()}.m4a"
        return File(cacheDir, fileName)
    }

    fun stopRecording() {
        if (!isRecording) return
        myMediaRecorder?.apply {
            try {
                stop()
                reset()
                release()
            } catch (error: IOException) {
                error.printStackTrace()
                Log.d ("my_log", "${error.printStackTrace()}")
            }

        }
        myMediaRecorder = null
        isRecording = false
    }

    fun getLastRecordedFilePath(): String? {
        return lastRecordedFilePath
    }
}

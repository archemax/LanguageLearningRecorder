package com.example.languagelearningrecorder

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaRecorder
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView

import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AudioRecorderClass(
    private val cacheDir: File,
    private val formatTextView: TextView,
    private val context: Context
) {

    private var myMediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var lastRecordedFilePath: String? = null
    private var defaultFormatIndex = 2
    private var selectedFormatIndex = 2
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val SELECTED_FORMAT_KEY = "selected_format"

    init {
        selectedFormatIndex = sharedPreferences.getInt(SELECTED_FORMAT_KEY, defaultFormatIndex)
        formatTextView.text = getAudioFormatName(selectedFormatIndex)
        changeRecordingFormat(selectedFormatIndex)
    }

    private fun getAudioFormatName(formatIndex: Int): String {
        return when (formatIndex) {
            0 -> "3gp"
            1 -> "ogg"
            2 -> "mpeg4"
            else -> "mpeg4"
        }
    }


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
        val formatExtension = getAudioFormatName(selectedFormatIndex)
        val fileName = "my_audio_${System.currentTimeMillis()}.$formatExtension"
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
                Log.d("my_log", "${error.printStackTrace()}")
            }

        }
        myMediaRecorder = null
        isRecording = false
    }

    fun getLastRecordedFilePath(): String? {
        return lastRecordedFilePath
    }

    fun showFormatSelectionDialog(context: Context, textView: TextView) {
        val audioFormats = arrayOf("3gp", "ogg", "mpeg4")
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Select Audio Format")
            .setItems(audioFormats) { _, format ->
                selectedFormatIndex = format
                changeRecordingFormat(format)
                textView.text = audioFormats[format]

                sharedPreferences.edit().putInt(SELECTED_FORMAT_KEY,format).apply()

            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnCancelListener {

                changeRecordingFormat(defaultFormatIndex)
                formatTextView.text = audioFormats[defaultFormatIndex]
            }

        builder.create().show()
    }

    private fun changeRecordingFormat(selectedIndex: Int) {
        val outputFormat: Int = when (selectedIndex) {
            0 -> MediaRecorder.OutputFormat.THREE_GPP
            1 -> MediaRecorder.OutputFormat.OGG
            else -> MediaRecorder.OutputFormat.MPEG_4

        }
        myMediaRecorder?.apply {
            setOutputFormat(outputFormat)
        }
    }
}

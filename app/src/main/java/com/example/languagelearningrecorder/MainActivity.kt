package com.example.languagelearningrecorder

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.languagelearningrecorder.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimerTask


class MainActivity : AppCompatActivity(), Timer.TimerInterface {

    private lateinit var binding: ActivityMainBinding
    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private lateinit var timer: Timer
    private var myAudioRecorder: AudioRecorderClass? = null

    //// onCreate//////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the cache directory
        val cacheDir = applicationContext.cacheDir
        myAudioRecorder = AudioRecorderClass(cacheDir, binding.selectFormatTextView)
        val myMediaPlayer = MediaPlayerClass()

        timer = Timer(this)


        permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted)
            ActivityCompat.requestPermissions(this, permissions, Companion.REQUEST_CODE)


        val recordButton = findViewById<ImageButton>(R.id.recordButton)
        val stopRecordButton = findViewById<ImageButton>(R.id.stopRecordButton)
        val timerTextView = findViewById<TextView>(R.id.timerTextView)



        recordButton.setOnClickListener {
            timer.start()
            stopRecordButton.visibility = View.VISIBLE
            myAudioRecorder!!.startRecording()

            binding.audioLevelView.setAudioRecorder(myAudioRecorder!!)
            binding.audioLevelView.startUpdatingLevel()


        }

        stopRecordButton.setOnClickListener {
            timer.stop()
            binding.audioLevelView.stopUpdatingLevel()
            binding.audioLevelView.updateLevel(0)


            timerTextView.text = "00:00:00"
            stopRecordButton.visibility = View.INVISIBLE
            myAudioRecorder!!.stopRecording()
            val filePath = myAudioRecorder!!.getLastRecordedFilePath()
            myMediaPlayer.playLastRecordedAudio(filePath)
        }



        binding.selectFormatTextView.setOnClickListener {
            onFormatTextViewClicked()
        }
    }

    private fun onFormatTextViewClicked(){
        myAudioRecorder?.showFormatSelectionDialog(this, binding.selectFormatTextView)
    }

    override fun startTimer(duration: String) {
        binding.timerTextView.text = duration
    }


    companion object {
        private const val REQUEST_CODE = 125
    }


}
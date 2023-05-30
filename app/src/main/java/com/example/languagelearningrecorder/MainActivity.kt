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


    //// onCreate//////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the cache directory
        val cacheDir = applicationContext.cacheDir
        val myAudioRecorder = AudioRecorderClass(cacheDir)
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

        recordButton.setOnClickListener {
            stopRecordButton.visibility = View.VISIBLE
            myAudioRecorder.startRecording()
        }
        stopRecordButton.setOnClickListener{
            stopRecordButton.visibility=View.INVISIBLE

            myAudioRecorder.stopRecording()
            val filePath = myAudioRecorder.getLastRecordedFilePath()
            myMediaPlayer.playLastRecordedAudio(filePath)
        }










    }


    override fun startTimer(duration: String) {
        binding.timerTextView.text = duration
    }


    companion object {
        private const val REQUEST_CODE = 125
    }
}
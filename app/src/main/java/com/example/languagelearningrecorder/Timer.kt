package com.example.languagelearningrecorder

import android.os.Handler
import android.os.Looper

class Timer(timerListener: TimerInterface) {
    interface TimerInterface {
        fun startTimer(duration: String)
    }

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable // interface
    private var duration = 0L
    private var delay = 100L


    init {
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            timerListener.startTimer(formatTime())
        }
    }


    fun start() {
        handler.postDelayed(runnable, delay)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
        duration = 0L
    }

    private fun formatTime(): String {
        val millis = duration % 1000
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60


        var formattedTime = "%02d:%02d:%02d".format(minutes, seconds, millis / 10)
        return formattedTime
    }
}
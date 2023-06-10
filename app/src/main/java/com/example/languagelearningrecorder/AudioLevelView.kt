package com.example.languagelearningrecorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaRecorder
import android.util.AttributeSet
import android.view.View

class AudioLevelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var level: Int = 0
    private val paint: Paint = Paint()
    private var audioRecorder: AudioRecorderClass? = null
    private var updateThread: Thread? = null
    private var isRunning = false

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }

    fun setAudioRecorder(audioRecorder: AudioRecorderClass) {
        this.audioRecorder = audioRecorder
    }

    fun startUpdatingLevel() {
        if (updateThread == null || !updateThread!!.isAlive) {
            isRunning = true
            updateThread = Thread(Runnable {
                while (isRunning) {
                    val audioLevel = audioRecorder?.getCurrentAudioLevel() ?: 0
                    post {
                        updateLevel(audioLevel)
                    }
                    try {
                        Thread.sleep(50) // Update every 50 milliseconds
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            })
            updateThread!!.start()
        }
    }

    fun stopUpdatingLevel() {
        isRunning = false
        updateThread?.join()
    }

    fun updateLevel(level: Int) {
        this.level = level
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val rectangleHeight = width * level / 100

        canvas?.drawRect(0f, height - rectangleHeight, width, height, paint)
    }
}
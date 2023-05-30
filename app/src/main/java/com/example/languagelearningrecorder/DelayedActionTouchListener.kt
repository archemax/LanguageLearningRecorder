package com.example.languagelearningrecorder

import android.view.MotionEvent
import android.view.View
import android.os.Handler
import androidx.core.os.postDelayed

class DelayedActionTouchListener: View.OnTouchListener {

    private var handler:Handler? = null
    private var isButtonEnabled = true

    private val  actionDownRunnable = Runnable{
        isButtonEnabled = true
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                if (isButtonEnabled){
                    handler = Handler()
                    handler?.postDelayed(actionDownRunnable, 1000)
                    isButtonEnabled = false



                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                handler?.removeCallbacks(actionDownRunnable)
                isButtonEnabled = true
            }
        }
        return true
    }
}
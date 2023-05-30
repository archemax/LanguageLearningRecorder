package com.example.languagelearningrecorder

import android.view.View
import android.view.MotionEvent

class MyTouchListener : View.OnTouchListener {

    private var isActionDownHandled = false

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isActionDownHandled = true
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isActionDownHandled) {
                    isActionDownHandled = false
                    return true
                }
            }
        }
        return false
    }

}

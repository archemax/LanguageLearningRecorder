package com.example.languagelearningrecorder

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class MediaPlayerClass() {
    private var myMediaPayer: MediaPlayer? = null

    fun playLastRecordedAudio (filePath : String?){
        if (filePath.isNullOrEmpty()) return

        myMediaPayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            start()
        }
    }

    fun stopPlaying(){
        myMediaPayer?.apply {
            stop()
            release()
        }
        myMediaPayer = null
    }

}


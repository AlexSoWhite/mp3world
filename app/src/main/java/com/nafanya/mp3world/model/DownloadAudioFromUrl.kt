package com.nafanya.mp3world.model

import android.content.Context
import android.os.AsyncTask
import java.io.BufferedInputStream
import java.net.URL
import kotlin.io.path.toPath

class DownloadAudioFromUrl(val context: Context) : AsyncTask<String, String, String>() {

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])
        val connection = url.openConnection()
        connection.connect()
        val inputStream = BufferedInputStream(url.openStream())
        val filename = url.toURI().toPath().last().toString()
//        val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
//        val data = ByteArray(1024)
//        var total:Long = 0
//        var count = 0
//        while (inputStream.read(data) != -1) {
//            count = inputStream.read(data)
//            total += count
//            outputStream.write(data, 0, count)
//        }
//        outputStream.flush()
//        outputStream.close()
//        inputStream.close()
//        println("finished saving audio.mp3 to internal storage")
        return "Success"
    }
}

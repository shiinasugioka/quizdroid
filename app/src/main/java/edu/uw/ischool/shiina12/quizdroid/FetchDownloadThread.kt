package edu.uw.ischool.shiina12.quizdroid

import android.util.Log

private const val TAG = "FetchDownloadThread"

class FetchDownloadThread: Thread() {
    override fun run() {
        val url = AppPreferences.getURL("downloadURL").toString()
        Log.i(TAG, "Curr URL: $url")

    }
}
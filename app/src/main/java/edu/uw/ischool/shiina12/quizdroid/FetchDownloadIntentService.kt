package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.JobIntentService
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FetchDownloadIntentService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        val url = AppPreferences.getURL("downloadURL").toString()

        if (isOnline(this)) {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.inputStream
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                saveToFile(jsonString)

            } catch (e: Exception) {
                sendBroadcastDownloadFailure()
            }

        } else {
            sendBroadcastNoInternet()
        }
    }

    private fun isOnline(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }

        return result
    }

    private fun saveToFile(jsonString: String) {
        try {
            val fileOutputStream = openFileOutput("questions.json", Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            sendBroadcastDownloadFailure()
        }
    }

    private fun sendBroadcastDownloadFailure() {
        val intent = Intent("DOWNLOAD_FAILED")
        sendBroadcast(intent)
    }

    private fun sendBroadcastNoInternet() {
        val intent = Intent("NO_INTERNET")
        sendBroadcast(intent)
    }

//    fun enqueueWork(context: Context, intent: Intent) {
//        enqueueWork(context, FetchDownloadIntentService::class.java, 1, intent)
//    }


    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, FetchDownloadIntentService::class.java, 1, intent)
        }
    }

}
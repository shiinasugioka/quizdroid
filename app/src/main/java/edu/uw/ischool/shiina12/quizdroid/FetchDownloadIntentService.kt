package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "DownloadService"

class FetchDownloadIntentService : JobIntentService() {
    override fun onCreate() {
        val intent = Intent(this, FetchDownloadIntentService::class.java)
        enqueueWork(this, FetchDownloadIntentService::class.java, 1, intent)
    }

    override fun onHandleWork(intent: Intent) {
        val url = AppPreferences.getURL("downloadURL").toString()

        if (isOnline(this)) {
            Log.i(TAG, "is online")
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.inputStream
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                saveToFile(jsonString)

            } catch (e: Exception) {
                sendBroadcastDownloadFailure(this)
            }

        } else {
            Log.i(TAG, "is not online")
            sendBroadcastNoInternet(this)
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
            sendBroadcastDownloadFailure(this)
        }
    }

    private fun sendBroadcastDownloadFailure(context: Context) {
        val intent = Intent("DOWNLOAD_FAILED")
        sendBroadcast(intent)
        Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show()
    }

    private fun sendBroadcastNoInternet(context: Context) {
        val intent = Intent("NO_INTERNET")
        sendBroadcast(intent)
        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
    }

}
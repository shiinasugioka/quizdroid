package edu.uw.ischool.shiina12.quizdroid

import android.app.Application
import android.util.Log
import android.content.Context


private const val TAG = "QuizApp"

class QuizApp : Application() {
    private val context: Context = this

    val topicRepository: TopicRepository by lazy { TopicRepositoryList(context) }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "started QuizApp Application")
        Log.d(TAG, topicRepository.toString())
    }
}

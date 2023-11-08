package edu.uw.ischool.shiina12.quizdroid

import android.app.Application
import android.util.Log
import android.content.Context


private const val TAG = "QuizApp"

class QuizApp : Application() {
    companion object {
        private var instance: QuizApp? = null

        fun getInstance(): QuizApp {
            return instance ?: QuizApp()
        }
    }

    lateinit var topicRepository: TopicRepository

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "started QuizApp Application")
        instance = this
        topicRepository = TopicRepositoryList(this)
    }

}

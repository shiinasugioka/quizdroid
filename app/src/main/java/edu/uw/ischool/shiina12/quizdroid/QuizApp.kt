package edu.uw.ischool.shiina12.quizdroid

import android.app.Application
import android.util.Log

private const val TAG = "QuizApp"

class QuizApp : Application() {
    lateinit var topicRepository: TopicRepository
    var correctAnswerCount: Int = 0
    var questionNumber: Int = 1
    var numTotalQuestions: Int = 0
    var currTopic: String = ""

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "started QuizApp Application")
        topicRepository = TopicRepositoryList()
    }

    fun reset() {
        correctAnswerCount = 0
        questionNumber = 1
        numTotalQuestions = 0
        currTopic = ""
    }

    fun incrementCorrectAnswerCount() {
        correctAnswerCount++
    }

    fun incrementQuestionNumber() {
        questionNumber++
    }

    fun setNewNumTotalQuestions(questionCount: Int) {
        numTotalQuestions = questionCount
    }

    fun setNewTopic(newTopic: String) {
        currTopic = newTopic
    }

}

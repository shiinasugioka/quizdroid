package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

private const val TAG = "MainActivity"

private const val TOPIC_OBJECT = "topicObject"

class MainActivity : AppCompatActivity() {
//    private val quizApp = QuizApp.getInstance()
//    private val topicRepo = quizApp.topicRepository

    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "started Main Activity")
        quizApp = QuizApp.getInstance()
        topicRepo = quizApp.topicRepository

//        Log.i(TAG, "quizApp: $quizApp")
//        Log.i(TAG, "topicRepo: $topicRepo")
//        topicRepo = quizApp.topicRepository
//        Log.i(TAG, "topicRepo: $topicRepo")

        val mathButton = findViewById<Button>(R.id.QuizOption1)
        val physicsButton = findViewById<Button>(R.id.QuizOption2)
        val marvelButton = findViewById<Button>(R.id.QuizOption3)

        mathButton.setOnClickListener {
            goToOverview("Math")
        }

        physicsButton.setOnClickListener {
            goToOverview("Physics")
        }

        marvelButton.setOnClickListener {
            goToOverview("Marvel Super Heroes")
        }

    }

    private fun goToOverview(selectedTopic: String) {
//        QuizData.setNewTopicName(selectedTopic)
//        val topicObject = topicRepo.getTopicByName(selectedTopic)
//        val topicRepo = quizApp.topicRepository
        val topicObj = topicRepo.getTopicByName(selectedTopic)
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        overviewIntent.putExtra(TOPIC_OBJECT, topicObj)

        startActivity(overviewIntent)
    }
}

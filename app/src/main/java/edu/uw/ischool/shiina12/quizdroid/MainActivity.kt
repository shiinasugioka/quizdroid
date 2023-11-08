package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "started Main Activity")

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

        val mathButton = findViewById<Button>(R.id.QuizOption1)
        val mathButtonSubtitle = findViewById<TextView>(R.id.QuizOption1Subtitle)
        val physicsButton = findViewById<Button>(R.id.QuizOption2)
        val physicsButtonSubtitle = findViewById<TextView>(R.id.QuizOption2Subtitle)
        val marvelButton = findViewById<Button>(R.id.QuizOption3)
        val marvelButtonSubtitle = findViewById<TextView>(R.id.QuizOption3Subtitle)

        val mathButtonSubtitleText = topicRepo.getTopicByName("Math")?.shortDescription
        mathButtonSubtitle.text = mathButtonSubtitleText
        val physicsButtonSubtitleText = topicRepo.getTopicByName("Physics")?.shortDescription
        physicsButtonSubtitle.text = physicsButtonSubtitleText
        val marvelButtonSubtitleText =
            topicRepo.getTopicByName("Marvel Super Heroes")?.shortDescription
        marvelButtonSubtitle.text = marvelButtonSubtitleText

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
        quizApp.setNewTopic(selectedTopic)
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        startActivity(overviewIntent)
    }
}

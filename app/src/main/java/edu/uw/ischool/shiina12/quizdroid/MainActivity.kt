package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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

        val mathTopicName = "Mathematics"
        val scienceTopicName = "Science!"
        val marvelTopicName = "Marvel Super Heroes"

        val mathButtonSubtitleText = topicRepo.getTopicByName(mathTopicName)?.shortDescription
        mathButtonSubtitle.text = mathButtonSubtitleText
        mathButton.text = mathTopicName

        val physicsButtonSubtitleText = topicRepo.getTopicByName(scienceTopicName)?.shortDescription
        physicsButtonSubtitle.text = physicsButtonSubtitleText
        physicsButton.text = scienceTopicName

        val marvelButtonSubtitleText =
            topicRepo.getTopicByName(marvelTopicName)?.shortDescription
        marvelButtonSubtitle.text = marvelButtonSubtitleText
        marvelButton.text = marvelTopicName

        mathButton.setOnClickListener {
            goToOverview(mathTopicName)
        }

        physicsButton.setOnClickListener {
            goToOverview(scienceTopicName)
        }

        marvelButton.setOnClickListener {
            goToOverview(marvelTopicName)
        }

    }

    private fun goToOverview(selectedTopic: String) {
        quizApp.setNewTopic(selectedTopic)
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        startActivity(overviewIntent)
    }
}

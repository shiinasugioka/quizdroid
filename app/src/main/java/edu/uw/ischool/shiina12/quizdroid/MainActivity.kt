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

        val topButton = findViewById<Button>(R.id.QuizOption1)
        val topButtonSubtitle = findViewById<TextView>(R.id.QuizOption1Subtitle)
        val middleButton = findViewById<Button>(R.id.QuizOption2)
        val middleButtonSubtitle = findViewById<TextView>(R.id.QuizOption2Subtitle)
        val bottomButton = findViewById<Button>(R.id.QuizOption3)
        val bottomButtonSubtitle = findViewById<TextView>(R.id.QuizOption3Subtitle)

        val listOfTopicNames = topicRepo.getTopicNames()
        Log.i(TAG, listOfTopicNames.toString())
        if (listOfTopicNames.size > 3) {
            throw Exception("You have too many topics.")
        }

        val firstTopicName = listOfTopicNames[0]
        val secondTopicName = listOfTopicNames[1]
        val thirdTopicName = listOfTopicNames[2]

//        val firstTopicName = "first"
//        val secondTopicName = "second"
//        val thirdTopicName = "third"

        val topButtonSubtitleText = topicRepo.getTopicByName(firstTopicName)?.shortDescription
        topButtonSubtitle.text = topButtonSubtitleText
        topButton.text = firstTopicName

        val middleButtonSubtitleText = topicRepo.getTopicByName(secondTopicName)?.shortDescription
        middleButtonSubtitle.text = middleButtonSubtitleText
        middleButton.text = secondTopicName

        val bottomButtonSubtitleText =
            topicRepo.getTopicByName(thirdTopicName)?.shortDescription
        bottomButtonSubtitle.text = bottomButtonSubtitleText
        bottomButton.text = thirdTopicName

        topButton.setOnClickListener {
            goToOverview(firstTopicName)
        }

        middleButton.setOnClickListener {
            goToOverview(secondTopicName)
        }

        bottomButton.setOnClickListener {
            goToOverview(thirdTopicName)
        }

    }

    private fun goToOverview(selectedTopic: String) {
        quizApp.setNewTopic(selectedTopic)
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        startActivity(overviewIntent)
    }
}

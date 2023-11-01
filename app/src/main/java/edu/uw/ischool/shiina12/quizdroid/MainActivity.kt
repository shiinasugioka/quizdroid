package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

private const val TOPIC_NAME = "topicName"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        overviewIntent.putExtra(TOPIC_NAME, selectedTopic)

        startActivity(overviewIntent)
    }
}
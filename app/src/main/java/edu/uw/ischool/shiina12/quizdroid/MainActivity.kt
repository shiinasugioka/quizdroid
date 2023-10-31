package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mathButton = findViewById<Button>(R.id.QuizOption1)
        val physicsButton = findViewById<Button>(R.id.QuizOption2)
        val marvelButton = findViewById<Button>(R.id.QuizOption3)

        mathButton.setOnClickListener {
            startQuiz("Math")
        }

        physicsButton.setOnClickListener {
            startQuiz("Physics")
        }

        marvelButton.setOnClickListener {
            startQuiz("Marvel Super Heroes")
        }

    }

    private fun startQuiz(selectedTopic: String) {
        val intent = Intent(this, TopicOverviewActivity::class.java)
        intent.putExtra("topicName", selectedTopic)
        startActivity(intent)
    }
}
package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val quizApp: QuizApp = application as QuizApp
        val topicRepo: TopicRepository = quizApp.topicRepository

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val userURLEditText = findViewById<EditText>(R.id.userInputURL)
        val userDownloadIntervalEditText = findViewById<EditText>(R.id.userInputDownloadInterval)
        val saveDataButton = findViewById<Button>(R.id.saveDataButton)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        saveDataButton.setOnClickListener {
            val newURL = userURLEditText.text.toString()
            topicRepo.setDataURL(newURL)

            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
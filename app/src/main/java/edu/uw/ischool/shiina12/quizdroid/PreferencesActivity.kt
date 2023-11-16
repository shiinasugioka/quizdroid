package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val userURLEditText = findViewById<EditText>(R.id.userInputURL)
        val userDownloadIntervalEditText = findViewById<EditText>(R.id.userInputDownloadInterval)
        val saveDataButton = findViewById<Button>(R.id.saveDataButton)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        saveDataButton.setOnClickListener {
            val newURL = userURLEditText.text.toString()
            val newDownloadInterval = userDownloadIntervalEditText.text.toString().toInt()

            AppPreferences.putNewURL("downloadURL", newURL)
            AppPreferences.putNewDownloadInterval("downloadInterval", newDownloadInterval)

            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
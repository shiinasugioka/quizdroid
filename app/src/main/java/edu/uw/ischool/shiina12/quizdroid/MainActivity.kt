package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

private const val TAG = "MainActivity"

object AppPreferences {
    private const val PREFERENCES_NAME = "AppPref"
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getURL(key: String, defaultURL: String): String? {
        return sharedPreferences.getString(key, defaultURL)
    }

    fun putNewURL(key: String, newURL: String) {
        sharedPreferences.edit().putString(key, newURL).apply()
    }

    fun getDownloadInterval(key: String, defaultDownloadInterval: Int): Int {
        return sharedPreferences.getInt(key, defaultDownloadInterval)
    }

    fun putNewDownloadInterval(key: String, newDownloadInterval: Int) {
        sharedPreferences.edit().putInt(key, newDownloadInterval).apply()
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "started MainActivity")

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

        val topButton = findViewById<Button>(R.id.QuizOption1)
        val topButtonSubtitle = findViewById<TextView>(R.id.QuizOption1Subtitle)
        val middleButton = findViewById<Button>(R.id.QuizOption2)
        val middleButtonSubtitle = findViewById<TextView>(R.id.QuizOption2Subtitle)
        val bottomButton = findViewById<Button>(R.id.QuizOption3)
        val bottomButtonSubtitle = findViewById<TextView>(R.id.QuizOption3Subtitle)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "QuizDroid!"

        val listOfTopicNames = topicRepo.getTopicNames()
        Log.i(TAG, "listOfTopicName: $listOfTopicNames")
        if (listOfTopicNames.size > 3) {
            throw Exception("You have too many topics. Found ${listOfTopicNames.size} not 3.")
        } else if (listOfTopicNames.size < 3) {
            throw Exception("You don't have enough topics. Found ${listOfTopicNames.size} not 3.")
        }

        val firstTopicName = listOfTopicNames[0]
        val secondTopicName = listOfTopicNames[1]
        val thirdTopicName = listOfTopicNames[2]

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

        Log.i(TAG, "button names: $firstTopicName, $secondTopicName, and $thirdTopicName")

        topButton.setOnClickListener {
            goToOverview(listOfTopicNames[0])
        }

        middleButton.setOnClickListener {
            goToOverview(secondTopicName)
        }

        bottomButton.setOnClickListener {
            goToOverview(thirdTopicName)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openPreferences()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openPreferences() {
        val preferencesIntent = Intent(this, PreferencesActivity::class.java)
        startActivity(preferencesIntent)
    }

    private fun goToOverview(selectedTopic: String) {
        quizApp.setNewTopic(selectedTopic)
        val overviewIntent = Intent(this, TopicOverviewActivity::class.java)
        startActivity(overviewIntent)
    }
}

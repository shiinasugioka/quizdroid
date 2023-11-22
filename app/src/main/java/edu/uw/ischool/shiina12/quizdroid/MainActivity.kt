package edu.uw.ischool.shiina12.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

object AppPreferences {
    private const val PREFERENCES_NAME = "AppPref"
    private lateinit var sharedPreferences: SharedPreferences
    private const val defaultURL = "https://tednewardsandbox.site44.com/questions.json"

    //        "https://raw.githubusercontent.com/shiinasugioka/quizdroid/storage/app/data/quiz_data_formatted.json"
    private const val defaultDownloadInterval = 60

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getURL(key: String): String? {
        return sharedPreferences.getString(key, defaultURL)
    }

    fun putNewURL(key: String, newURL: String) {
        sharedPreferences.edit().putString(key, newURL).apply()
    }

    fun getDownloadInterval(key: String): Int {
        return sharedPreferences.getInt(key, defaultDownloadInterval)
    }

    fun putNewDownloadInterval(key: String, newDownloadInterval: Int) {
        sharedPreferences.edit().putInt(key, newDownloadInterval).apply()
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    private lateinit var topButton: Button
    private lateinit var topButtonSubtitle: TextView
    private lateinit var middleButton: Button
    private lateinit var middleButtonSubtitle: TextView
    private lateinit var bottomButton: Button
    private lateinit var bottomButtonSubtitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "started MainActivity")

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

        topButton = findViewById(R.id.QuizOption1)
        topButtonSubtitle = findViewById(R.id.QuizOption1Subtitle)
        middleButton = findViewById(R.id.QuizOption2)
        middleButtonSubtitle = findViewById(R.id.QuizOption2Subtitle)
        bottomButton = findViewById(R.id.QuizOption3)
        bottomButtonSubtitle = findViewById(R.id.QuizOption3Subtitle)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "QuizDroid!"

        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            topicRepo.loadTopicsFromURL(AppPreferences.getURL("downloadURL").toString())
            setUIElements()
        }

        val downloadInterval = AppPreferences.getDownloadInterval("downloadInterval")
        setupDownloadService(downloadInterval)
    }

    // threading intent
    //

    private fun setupDownloadService(downloadInterval: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // here

        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val triggerTime = SystemClock.elapsedRealtime() + downloadInterval * 60 * 1000
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            (downloadInterval * 60 * 1000).toLong(),
            pendingIntent
        )
    }

    override fun onStart() {
        super.onStart()
        setUIElements()
    }

    private fun setUIElements() {
        val listOfTopicNames: List<String> = topicRepo.getTopicNames()

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

        topButton.setOnClickListener { goToOverview(firstTopicName) }
        middleButton.setOnClickListener { goToOverview(secondTopicName) }
        bottomButton.setOnClickListener { goToOverview(thirdTopicName) }
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

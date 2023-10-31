package edu.uw.ischool.shiina12.quizdroid

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.InputStream

private const val TAG = "TopicOverviewActivity"

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val quizWelcomeText = findViewById<TextView>(R.id.quiz_welcome_text)
        val quizDescription = findViewById<TextView>(R.id.quiz_description)
        val quizTotalQuestions = findViewById<TextView>(R.id.quiz_total_questions)

        val selectedTopic = intent.getStringExtra("topicName")
        val welcomeMessage = "Welcome to the $selectedTopic Quiz"
        quizWelcomeText.text = welcomeMessage

        Log.i(TAG, "welcome message: $welcomeMessage")

        try {
            Log.i(TAG, "open input stream")
            val inputStream: InputStream = assets.open("app/src/main/assets/quiz_data.json")
            Log.i(TAG, "opened json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Log.i(TAG, "bufferedReader")
            val jsonRoot = JSONObject(jsonString)
            Log.i(TAG, "created jsonRoot")
            val topics = jsonRoot.getJSONArray("topics")
            Log.i(TAG, "success")

            for (i in 0 until topics.length()) {
                val topic = topics.getJSONObject(i)
                Log.i(TAG, "topic in json: $topic")

                if (topic.getString("name") == selectedTopic) {
                    quizDescription.text = topic.getString("description")
                    val numQuestions = topic.getJSONArray("questions").length()
                    val quizTotalQuestionsTextView = "Total Questions: $numQuestions"
                    quizTotalQuestions.text = quizTotalQuestionsTextView
                }
            }

        } catch(e: Exception) {
            Log.i(TAG, "catch: failed to read from json file")
        }

    }
}
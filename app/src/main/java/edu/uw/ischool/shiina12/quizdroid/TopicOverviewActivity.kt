package edu.uw.ischool.shiina12.quizdroid

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.InputStream

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val quizWelcomeText = findViewById<TextView>(R.id.quiz_welcome_text)
        val quizDescription = findViewById<TextView>(R.id.quiz_welcome_text)
        val quizTotalQuestions = findViewById<TextView>(R.id.quiz_total_questions)

        val selectedTopic = intent.getStringExtra("topicName")
        val welcomeMessage = "Welcome to the $selectedTopic Quiz"
        quizWelcomeText.text = welcomeMessage

        try {
            val inputStream: InputStream = assets.open("assets/quiz_data.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonRoot = JSONObject(jsonString)
            val topics = jsonRoot.getJSONArray("topics")

            for (i in 0 until topics.length()) {
                val topic = topics.getJSONObject(i)
                if (topic.getString("name") == selectedTopic) {
                    quizDescription.text = topic.getString("description")
                    val numQuestions = topic.getJSONArray("questions").length()
                    quizTotalQuestions.text = "Total Questions: $numQuestions"
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
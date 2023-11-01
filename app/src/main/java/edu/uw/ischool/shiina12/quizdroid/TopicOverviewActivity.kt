package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.InputStream

private const val TAG = "TopicOverviewActivity"
private const val TOPIC_NAME = "topicName"

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val quizWelcomeText = findViewById<TextView>(R.id.quiz_welcome_text)
        val quizDescription = findViewById<TextView>(R.id.quiz_description)
        val quizTotalQuestions = findViewById<TextView>(R.id.quiz_total_questions)

        val beginQuizButton = findViewById<Button>(R.id.start_quiz_button)

        lateinit var topicName: String

        val selectedTopic = intent.getStringExtra(TOPIC_NAME)
        val welcomeMessage = "Welcome to the $selectedTopic Quiz"
        quizWelcomeText.text = welcomeMessage

        try {
            val inputStream: InputStream = assets.open("quiz_data.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonRoot = JSONObject(jsonString)
            val topics = jsonRoot.getJSONArray("topics")

            for (i in 0 until topics.length()) {
                val topic = topics.getJSONObject(i)

                if (topic.getString("name") == selectedTopic) {
                    if (selectedTopic != null) topicName = selectedTopic
                    quizDescription.text = topic.getString("description")
                    val numQuestions = topic.getJSONArray("questions").length()
                    val quizTotalQuestionsTextView = "Total Questions: $numQuestions"
                    quizTotalQuestions.text = quizTotalQuestionsTextView
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "catch: failed to read from json file")
        }

        beginQuizButton.setOnClickListener {
            beginQuiz(topicName)
        }
    }

    private fun beginQuiz(selectedTopic: String) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra(TOPIC_NAME, selectedTopic)
        startActivity(intent)
    }
}
package edu.uw.ischool.shiina12.quizdroid

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

private const val TAG = "QuestionsActivity"
private const val TOPIC_NAME = "topicName"

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // retrieve intents
        val topicName = intent.getStringExtra(TOPIC_NAME)
        Log.i(TAG, "received intent: $topicName")

        // retrieve all TextViews and Buttons
        val questionNumberTextView = findViewById<TextView>(R.id.questions_question_number)
        val questionDescriptionTextView =
            findViewById<TextView>(R.id.questions_questions_description)
        // radio buttons
        val firstRadioButton = findViewById<RadioButton>(R.id.questions_first_radio_button)
        val secondRadioButton = findViewById<RadioButton>(R.id.questions_second_radio_button)
        val thirdRadioButton = findViewById<RadioButton>(R.id.questions_third_radio_button)
        val fourthRadioButton = findViewById<RadioButton>(R.id.questions_fourth_radio_button)

        // save entire json object in a jsonRoot called `topics`
        val inputStream: InputStream = assets.open("quiz_data.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonRoot = JSONObject(jsonString)
        val topics = jsonRoot.getJSONArray("topics")

        // keep track of question index and list of questions
        var currentQuestionIndex = 0
        lateinit var questionsArray: JSONArray

        // retrieve array of questions
        for (i in 0 until topics.length()) {
            val topic = topics.getJSONObject(i)

            if (topic.getString("name") == topicName) {
                questionsArray = topic.getJSONArray("questions")
            }
        }


    }
}
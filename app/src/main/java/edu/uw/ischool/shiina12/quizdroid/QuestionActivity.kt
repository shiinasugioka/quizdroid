package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

private const val TAG = "QuestionsActivity"

private const val IS_CORRECT = "isCorrect"
private const val SELECTED_OPTION_TEXT = "selectedOptionText"
private const val CORRECT_ANS = "correctOptionText"

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // retrieve all TextViews and Buttons
        val questionNumberTextView = findViewById<TextView>(R.id.questions_question_number)
        val questionDescriptionTextView =
            findViewById<TextView>(R.id.questions_questions_description)
        val radioGroup = findViewById<RadioGroup>(R.id.questions_radio_buttons_group)
        val firstRadioButton = findViewById<RadioButton>(R.id.questions_first_radio_button)
        val secondRadioButton = findViewById<RadioButton>(R.id.questions_second_radio_button)
        val thirdRadioButton = findViewById<RadioButton>(R.id.questions_third_radio_button)
        val fourthRadioButton = findViewById<RadioButton>(R.id.questions_fourth_radio_button)
        val submitButton = findViewById<Button>(R.id.questions_submit_button)

        submitButton.isEnabled = false

        // save entire json object in a jsonRoot called `topics`
        val inputStream: InputStream = assets.open("quiz_data.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonRoot = JSONObject(jsonString)
        val topics = jsonRoot.getJSONArray("topics")

        lateinit var questionsArray: JSONArray

        // retrieve array of questions
        for (i in 0 until topics.length()) {
            val topic = topics.getJSONObject(i)

            if (topic.getString("name") == QuizData.topicName) {
                questionsArray = topic.getJSONArray("questions")
            }
        }

        val questionNum = QuizData.questionNumber
        val currQuestion = questionsArray.getJSONObject(questionNum - 1)

        val questionText = currQuestion.getString("question")
        val currAnswerOptions = currQuestion.getJSONArray("options")
        val option1 = currAnswerOptions.getString(0)
        val option2 = currAnswerOptions.getString(1)
        val option3 = currAnswerOptions.getString(2)
        val option4 = currAnswerOptions.getString(3)

        questionNumberTextView.text = "Question " + questionNum
        questionDescriptionTextView.text = questionText
        firstRadioButton.text = option1
        secondRadioButton.text = option2
        thirdRadioButton.text = option3
        fourthRadioButton.text = option4

        var selectedOptionIndex: Int = -1
        radioGroup.setOnCheckedChangeListener { _, selectedId ->
            val isOptionSelected = selectedId != -1
            submitButton.isEnabled = isOptionSelected
            val radioButtonID = radioGroup.checkedRadioButtonId
            val selectedButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            selectedOptionIndex = radioGroup.indexOfChild(selectedButton)
        }

        submitButton.setOnClickListener {
            submitAnswer(selectedOptionIndex, currQuestion)
        }
    }

    private fun submitAnswer(
        selectedOptionIndex: Int,
        currQuestion: JSONObject
    ) {
        val correctAnswerIndex = currQuestion.getInt("correctAnswer")
        val isCorrect = (selectedOptionIndex == correctAnswerIndex)
        val selectedOptionText = currQuestion.getJSONArray("options").getString(selectedOptionIndex)
        val correctOptionText = currQuestion.getJSONArray("options").getString(correctAnswerIndex)

        val answerIntent = Intent(this, AnswerActivity::class.java)
        answerIntent.putExtra(IS_CORRECT, isCorrect)
        answerIntent.putExtra(SELECTED_OPTION_TEXT, selectedOptionText)
        answerIntent.putExtra(CORRECT_ANS, correctOptionText)

        startActivity(answerIntent)
    }
}
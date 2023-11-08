package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "QuestionsActivity"

private const val IS_CORRECT = "isCorrect"
private const val SELECTED_INDEX = "selectedIndex"

class QuestionActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

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

        // save json topic
        val topicName = quizApp.currTopic
        val topicObject = topicRepo.getTopicByName(topicName)

        // retrieve array of questions
        val questionsArray = topicObject?.listOfQuestions

        // calculate and set on screen values
        val questionNumTextViewText = "Question " + quizApp.questionNumber
        val currQuestion = questionsArray?.get(quizApp.questionNumber - 1)
        val questionText = currQuestion?.question
        val currAnswerOptions = currQuestion?.answers
        val option1 = currAnswerOptions?.get(0)
        val option2 = currAnswerOptions?.get(1)
        val option3 = currAnswerOptions?.get(2)
        val option4 = currAnswerOptions?.get(3)

        questionNumberTextView.text = questionNumTextViewText
        questionDescriptionTextView.text = questionText
        firstRadioButton.text = option1
        secondRadioButton.text = option2
        thirdRadioButton.text = option3
        fourthRadioButton.text = option4

        // radiobutton input
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
        currQuestion: Quiz?
    ) {
        val correctAnswerIndex = currQuestion?.correctAnswer
        val isCorrect = (selectedOptionIndex == correctAnswerIndex)

        val answerIntent = Intent(this, AnswerActivity::class.java)
        answerIntent.putExtra(IS_CORRECT, isCorrect)
        answerIntent.putExtra(SELECTED_INDEX, selectedOptionIndex)

        startActivity(answerIntent)
    }
}
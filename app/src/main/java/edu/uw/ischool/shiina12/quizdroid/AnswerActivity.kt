package edu.uw.ischool.shiina12.quizdroid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AnswerActivity"
private const val IS_CORRECT = "isCorrect"
private const val CURR_QUESTION_INDEX = "currentQuestionIndex"
private const val SELECTED_OPTION_INDEX = "selectedOptionIndex"
private const val SELECTED_OPTION_TEXT = "selectedOptionText"
private const val CORRECT_ANS = "correctOptionText"

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        // retrieve intents
        val isCorrect = intent.getBooleanExtra(IS_CORRECT, true)
        val currentQuestionIndex = intent.getIntExtra(CURR_QUESTION_INDEX, -1)
        val selectedOptionIndex = intent.getIntExtra(SELECTED_OPTION_INDEX, -1)
        val selectedOptionText = intent.getStringExtra(SELECTED_OPTION_TEXT)
        val correctOptionText = intent.getStringExtra(CORRECT_ANS)

        // retrieve TextViews and Buttons
        val questionNumberTextView = findViewById<TextView>(R.id.questions_question_number)
        val userAnswerTextView = findViewById<TextView>(R.id.questions_user_answers_display)
        val answerValidityTextView = findViewById<TextView>(R.id.questions_answers_validity)
        val correctAnswerWasTextView =
            findViewById<TextView>(R.id.questions_answers_answer_key_text)
        val realAnswerTextView = findViewById<TextView>(R.id.questions_real_answers_display)
        val correctAnswersCountTextView = findViewById<TextView>(R.id.questions_answers_count_text)
        val nextButton = findViewById<Button>(R.id.answers_next_button)

        nextButton.isEnabled = true

        questionNumberTextView.text = "Question $currentQuestionIndex"
        userAnswerTextView.text = selectedOptionText

        

    }
}

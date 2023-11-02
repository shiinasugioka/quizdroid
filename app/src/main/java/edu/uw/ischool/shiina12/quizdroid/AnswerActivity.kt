package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AnswerActivity"
private const val IS_CORRECT = "isCorrect"
private const val SELECTED_OPTION_TEXT = "selectedOptionText"
private const val CORRECT_ANS = "correctOptionText"

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        // retrieve intents
        val isCorrect = intent.getBooleanExtra(IS_CORRECT, true)
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

        questionNumberTextView.text = "Question ${QuizData.questionNumber}"
        userAnswerTextView.text = selectedOptionText
        realAnswerTextView.text = correctOptionText

        if (isCorrect) {
            answerValidityTextView.text = "Correct!"
            answerValidityTextView.setTextColor(resources.getColor(R.color.green))
            correctAnswerWasTextView.visibility = View.GONE
            realAnswerTextView.visibility = View.GONE
            QuizData.incrementCorrectAnswerCount()
        } else {
            answerValidityTextView.text = "Incorrect"
            answerValidityTextView.setTextColor(resources.getColor(R.color.red))
            correctAnswerWasTextView.visibility = View.VISIBLE
            realAnswerTextView.visibility = View.VISIBLE
        }

        if (QuizData.questionNumber == QuizData.numTotalQuestions) {
            nextButton.text = "FINISH"
            correctAnswersCountTextView.text =
                "You answered ${QuizData.correctAnswerCount} of ${QuizData.numTotalQuestions} correct on this quiz."
            nextButton.setOnClickListener {
                endQuiz()
            }
        } else {
            correctAnswersCountTextView.text =
                "You have ${QuizData.correctAnswerCount} of ${QuizData.numTotalQuestions} correct so far."
            nextButton.setOnClickListener {
                goToNextQuestion()
            }
        }
    }

    private fun goToNextQuestion() {
        QuizData.incrementQuestionNumber()

        val questionIntent = Intent(this, QuestionActivity::class.java)

        startActivity(questionIntent)
    }

    private fun endQuiz() {
        QuizData.reset()

        val mainIntent = Intent(this, MainActivity::class.java)

        startActivity(mainIntent)
    }
}

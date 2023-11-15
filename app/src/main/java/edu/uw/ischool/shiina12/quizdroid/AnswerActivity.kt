package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AnswerActivity"

private const val IS_CORRECT = "isCorrect"
private const val SELECTED_INDEX = "selectedIndex"

class AnswerActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "QuizDroid!"

        // save json topic
        val topicName = quizApp.currTopic
        val topicObject = topicRepo.getTopicByName(topicName)
        val questionsArray = topicObject?.listOfQuestions
        val currQuestion = questionsArray?.get(quizApp.questionNumber - 1)

        // retrieve intents
        val isCorrect = intent.getBooleanExtra(IS_CORRECT, true)
        val selectedOptionIndex = intent.getIntExtra(SELECTED_INDEX, 0)

        val correctOptionIndex = currQuestion?.correctAnswer

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

        val questionNumTextViewText = "Question " + quizApp.questionNumber
        questionNumberTextView.text = questionNumTextViewText
        if (currQuestion != null) {
            userAnswerTextView.text = currQuestion.answers[selectedOptionIndex]
            realAnswerTextView.text = currQuestion.answers[correctOptionIndex!!]
        }

        if (isCorrect) {
            answerValidityTextView.text = "Correct!"
            answerValidityTextView.setTextColor(resources.getColor(R.color.green))
            correctAnswerWasTextView.visibility = View.GONE
            realAnswerTextView.visibility = View.GONE
            quizApp.incrementCorrectAnswerCount()
        } else {
            answerValidityTextView.text = "Incorrect"
            answerValidityTextView.setTextColor(resources.getColor(R.color.red))
            correctAnswerWasTextView.visibility = View.VISIBLE
            realAnswerTextView.visibility = View.VISIBLE
        }

        val correctAnswersCountTextViewText: String
        if (quizApp.questionNumber == quizApp.numTotalQuestions) {
            nextButton.text = "FINISH"
            correctAnswersCountTextViewText =
                "You answered ${quizApp.correctAnswerCount} of ${quizApp.numTotalQuestions} correct on this quiz."
            correctAnswersCountTextView.text = correctAnswersCountTextViewText
            nextButton.setOnClickListener {
                endQuiz()
            }
        } else {
            correctAnswersCountTextViewText =
                "You have ${quizApp.correctAnswerCount} of ${quizApp.numTotalQuestions} correct so far."
            correctAnswersCountTextView.text = correctAnswersCountTextViewText
            nextButton.setOnClickListener {
                goToNextQuestion()
            }
        }
    }

    private fun goToNextQuestion() {
        quizApp.incrementQuestionNumber()

        val questionIntent = Intent(this, QuestionActivity::class.java)
        startActivity(questionIntent)
    }

    private fun endQuiz() {
        quizApp.reset()

        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}

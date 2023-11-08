package edu.uw.ischool.shiina12.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "TopicOverviewActivity"

class TopicOverviewActivity : AppCompatActivity() {
    private lateinit var quizApp: QuizApp
    private lateinit var topicRepo: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        quizApp = application as QuizApp
        topicRepo = quizApp.topicRepository

        // find UI elements
        val quizWelcomeText = findViewById<TextView>(R.id.quiz_welcome_text)
        val quizDescription = findViewById<TextView>(R.id.quiz_description)
        val quizTotalQuestions = findViewById<TextView>(R.id.quiz_total_questions)
        val beginQuizButton = findViewById<Button>(R.id.start_quiz_button)

        val topicName = quizApp.currTopic
        val topicObject = (topicRepo.getTopicByName(topicName) as Topic)

        val welcomeMessageText = "Welcome to the ${topicObject.title} Quiz"
        quizWelcomeText.text = welcomeMessageText
        quizDescription.text = topicObject.longDescription
        quizApp.setNewNumTotalQuestions(topicObject.listOfQuestions.size)
        val num = quizApp.numTotalQuestions
        val quizTotalQuestionsText = "Total Questions: $num"
        quizTotalQuestions.text = quizTotalQuestionsText

        beginQuizButton.setOnClickListener {
            beginQuiz()
        }
    }

    private fun beginQuiz() {
        val questionIntent = Intent(this, QuestionActivity::class.java)
        startActivity(questionIntent)
    }
}